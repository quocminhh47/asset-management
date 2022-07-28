package com.nashtech.assetmanagement.controller.rest.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.service.AssetService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin/api/assets")
public class AssetController {

	@Autowired
	private AssetService assetService;

	@GetMapping("/{userId}")
	public ResponseEntity<ListAssetResponseDto> getListAsset(
			@PathVariable("userId") String userId,
			@RequestParam(required = true, name = "page") Integer page,
			@RequestParam(required = true, name = "size") Integer size,
			@RequestParam(required = false, defaultValue = "", value = "assetcode") String assetcode,
			@RequestParam(required = false, defaultValue = "", value = "assetname") String assetname,
			@RequestParam(required = false, defaultValue = "", value = "categoryId") String categoryId,
			@RequestParam(required = false, defaultValue = "", value = "state") List<String> state) {
		ListAssetResponseDto result = assetService.getListAsset
				(userId, page, size, categoryId, assetcode, assetname ,state);
		return new ResponseEntity<ListAssetResponseDto>(result, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<AssetResponseDto> getOne(@RequestParam(required = true, name = "assetId") String assetId) {
		return new ResponseEntity<AssetResponseDto>(assetService.getOne(assetId), HttpStatus.OK);
	}
}
