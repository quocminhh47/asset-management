package com.nashtech.assetmanagement.utils;

public class TotalPages {
	
	public static long totalPages(long totalItems ,int itemsDisplay) {
		long pages = (long) 0;
		if(totalItems <= itemsDisplay) {
			pages =  1;
		}else if(totalItems > itemsDisplay) {
			long mod = totalItems % itemsDisplay;
			if (mod > 0) {
				pages = (long) (Math.floor(totalItems / itemsDisplay) + 1);
			}else {
				pages = totalItems / itemsDisplay;
			}
		}
		return pages;
	}
	
}
