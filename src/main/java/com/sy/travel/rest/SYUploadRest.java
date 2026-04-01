package com.sy.travel.rest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sy.travel.common.AjaxResult;

@RestController
@RequestMapping(value="/sy/upload", produces=MediaType.APPLICATION_JSON_VALUE)
public class SYUploadRest {
	@RequestMapping(value="/file", method=RequestMethod.POST)
	public AjaxResult<String> upload(MultipartFile file) {
		if(file.isEmpty()) {
			return new AjaxResult<String>(500, "failed", "file is null");
		}
		try {
			FileUtils.writeByteArrayToFile(new File("D:\\upload\\" + file.getOriginalFilename()), file.getBytes());
			return AjaxResult.success("ok");
		} catch (IOException e) {
			e.printStackTrace();
			return new AjaxResult<String>(500, "failed", "failed");
		}
	}
}
