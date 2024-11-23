package org.graduate.shoefastbe.service;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.*;
import org.graduate.shoefastbe.mapper.ProductMapper;
import org.graduate.shoefastbe.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RecommendationService {
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final CustomRepository customRepository;
    private final SalesRepository salesRepository;
    private final ImageRepository imageRepository;
    private final BrandsRepository brandsRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductAccountLikeMapRepository productAccountLikeMapRepository;
    // Hàm kết hợp các đặc điểm của sản phẩm (view, name, description)
    private String combineFeatures(String view, String name, String description) {
        return view + " " + name + " " + description;
    }
    public Page<ProductDtoResponse> getRecommendations(Long productId, Pageable pageable) throws IOException {
        Product productEntity = productRepository.findById(productId).orElse(null);
        if (productEntity == null) {
            throw new RuntimeException("Invalid product ID");
        }
        List<Product> products = productRepository.findAll();

        // Bước 1: Tạo danh sách các features của sản phẩm
        List<String> featuresList = products.stream()
                .map(product -> combineFeatures(String.valueOf(product.getView()), product.getName(), product.getDescription()))
                .collect(Collectors.toList());

        // Bước 2: Tính toán TF-IDF
        List<Map<String, Integer>> tfidfMatrix = calculateTfIdf(featuresList);

        // Bước 3: Tính Cosine Similarity
        RealMatrix similarityMatrix = calculateCosineSimilarity(tfidfMatrix);

        // Bước 4: Tìm sản phẩm tương tự dựa trên cosine similarity
        int indexProduct = -1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                indexProduct = i;
                break;
            }
        }

        if (indexProduct == -1) {
            throw new RuntimeException("Product not found.");
        }

        // Lấy danh sách sản phẩm tương tự theo cosine similarity
        Map<Integer, Double> similarProducts = new HashMap<>();
        for (int i = 0; i < similarityMatrix.getRowDimension(); i++) {
            similarProducts.put(i, similarityMatrix.getEntry(indexProduct, i));
        }

        // Sắp xếp sản phẩm theo similarity
        List<Integer> sortedProductIds = similarProducts.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Product> productList = new ArrayList<>();
        Map<Long, Double> productSimilarityMap = new HashMap<>();
        for (int i = 0; i < Math.min(3, sortedProductIds.size()); i++) {
            Product product = products.get(sortedProductIds.get(i));
            Double cosineValue = similarProducts.get(sortedProductIds.get(i));
            productList.add(product);
            productSimilarityMap.put(product.getId(), cosineValue); // similarityValue map
        }
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        return getProductDtoResponses(productPage, productSimilarityMap);
    }

    private Page<ProductDtoResponse> getProductDtoResponses(Page<Product> productEntities,Map<Long, Double> productSimilarityMap) {
        List<Attribute> attributeEntities = customRepository.getAttributeByProductId(productEntities
                .stream().map(Product::getId).collect(Collectors.toSet()));

        Map<Long, Attribute> attributeMap = attributeEntities.stream().collect(Collectors.toMap(
                Attribute::getProductId, Function.identity()
        ));
        List<Brands> brandsEntities = brandsRepository.findAllByIdIn(productEntities
                .stream()
                .map(Product::getBrandId)
                .collect(Collectors.toSet()));
        Map<Long, Brands> brandsEntityMap = brandsEntities.stream().collect(Collectors.toMap(
                Brands::getId,Function.identity()
        ));

        List<Sales> salesEntities = salesRepository.findAllByIdIn(productEntities
                .stream()
                .map(Product::getSaleId)
                .collect(Collectors.toSet()));
        Map<Long, Sales> salesEntityMap = salesEntities.stream().collect(Collectors.toMap(
                Sales::getId,Function.identity()
        ));
        Map<Long, Image> imageMap = imageRepository.findAllByProductIdIn(productEntities
                        .stream().map(Product::getId).collect(Collectors.toList()))
                .stream().filter(image -> image.getName().equals("main"))
                .collect(Collectors.toMap(Image::getProductId, Function.identity()));

        return productEntities.map(
                product -> {
                    Attribute attribute = attributeMap.get(product.getId());
                    return ProductDtoResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(attribute.getPrice())
                            .brand(brandsEntityMap.get(product.getBrandId()).getName())
                            .code(product.getCode())
                            .view(product.getView())
                            .description(product.getDescription())
                            .image(imageMap.get(product.getId()).getImageLink())
                            .discount(salesEntityMap.get(product.getSaleId()).getDiscount())
                            .isActive(product.getIsActive())
                            .liked(Boolean.FALSE)
                            .similarity(productSimilarityMap.get(product.getId()))
                            .build();
                }
        );
    }

    // Hàm tính TF-IDF matrix
    private List<Map<String, Integer>> calculateTfIdf(List<String> featuresList) throws IOException {
        // Tạo một RAMDirectory để chứa các chỉ mục của Lucene
        RAMDirectory ramDirectory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(ramDirectory, config);

        // Tạo các tài liệu Lucene từ các features của sản phẩm
        for (String features : featuresList) {
            Document doc = new Document();
            // Sử dụng TextField thay cho Field.Index.ANALYZED
            doc.add(new TextField("content", features, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();

        // Tạo chỉ mục và tính toán TF-IDF
        IndexReader reader = DirectoryReader.open(ramDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);

        List<Map<String, Integer>> tfidfMatrix = new ArrayList<>();
        for (int i = 0; i < reader.numDocs(); i++) {
            Document doc = reader.document(i);
            String text = doc.get("content");

            Map<String, Integer> termFrequency = new HashMap<>();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            TokenStream tokenStream = analyzer.tokenStream("content", text);
            tokenStream.reset();
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
            }
            tokenStream.end();
            tokenStream.close();

            tfidfMatrix.add(termFrequency);
        }

        return tfidfMatrix;
    }


    // Hàm tính Cosine Similarity giữa các vector TF-IDF
    private RealMatrix calculateCosineSimilarity(List<Map<String, Integer>> tfidfMatrix) {
        // Tạo ma trận TF-IDF từ các termFrequency
        List<List<Double>> tfidfVectors = new ArrayList<>();
        Set<String> allTerms = new HashSet<>();
        for (Map<String, Integer> termFrequency : tfidfMatrix) {
            allTerms.addAll(termFrequency.keySet());
        }

        for (Map<String, Integer> termFrequency : tfidfMatrix) {
            List<Double> vector = new ArrayList<>();
            for (String term : allTerms) {
                vector.add((double) termFrequency.getOrDefault(term, 0));
            }
            tfidfVectors.add(vector);
        }

        // Chuyển ma trận TF-IDF thành RealMatrix
        double[][] tfidfArray = new double[tfidfVectors.size()][tfidfVectors.get(0).size()];
        for (int i = 0; i < tfidfVectors.size(); i++) {
            for (int j = 0; j < tfidfVectors.get(i).size(); j++) {
                tfidfArray[i][j] = tfidfVectors.get(i).get(j);
            }
        }

        RealMatrix matrix = new Array2DRowRealMatrix(tfidfArray);
        return cosineSimilarity(matrix);
    }

    // Hàm tính cosine similarity giữa hai vector
    private RealMatrix cosineSimilarity(RealMatrix matrix) {
        int rowCount = matrix.getRowDimension();
        double[][] similarityMatrix = new double[rowCount][rowCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                similarityMatrix[i][j] = cosineSimilarity(matrix.getRow(i), matrix.getRow(j));
            }
        }

        return new Array2DRowRealMatrix(similarityMatrix);
    }

    // Hàm tính cosine similarity giữa hai vector
    private double cosineSimilarity(double[] vector1, double[] vector2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            normA += Math.pow(vector1[i], 2);
            normB += Math.pow(vector2[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
