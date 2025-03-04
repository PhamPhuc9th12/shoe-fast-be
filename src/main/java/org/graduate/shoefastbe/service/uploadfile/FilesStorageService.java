package org.graduate.shoefastbe.service.uploadfile;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file);

  List<String> upload(MultipartFile[] files);
  public Resource load(String filename);

  public void deleteAll();

  public Stream<Path> loadAll();
}
