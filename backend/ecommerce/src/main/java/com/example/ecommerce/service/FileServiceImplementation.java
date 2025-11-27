package com.example.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImplementation implements FileService {
	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		
		// extracting the filename of the uploaded file
		String originalFileName = file.getOriginalFilename();
		
		// Generating unique file name with extension
		String randomId = UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		String fileName = randomId.concat(extension);
		
		// Final file path
		String filePath = path + File.separator + fileName;
		
		
		// Creting folder if doesnt exists:-
		File folder = new File(path);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		// Upload to server(replace if exists):-
		Files.copy(file.getInputStream(), Paths.get(filePath),StandardCopyOption.REPLACE_EXISTING);
				// inputStream : gives you a stream of bytes , so you can read or process it.
		
		
		return fileName;
	}
}
