/*global cordova,window,console*/
/**
 * An Image Picker plugin for Cordova
 * 
 * Developed by Wymsee for Sync OnSet
 */

var ImagePicker = function() {
	
};

/*
*	success - success callback
*	fail - error callback
*	options
*		.maximumImagesCount - max images to be selected, defaults to 15. If this is set to 1, 
*		                      upon selection of a single image, the plugin will return it.
*		.width - width to resize image to (if one of height/width is 0, will resize to fit the
*		         other while keeping aspect ratio, if both height and width are 0, the full size
*		         image will be returned)
*		.height - height to resize image to
*		.quality - quality of resized image, defaults to 100
*/
ImagePicker.prototype.getPictures = function(success, fail, options) {
	if (!options) {
		options = {};
	}
	
	var params = {
		maximumImagesCount: options.maximumImagesCount ? options.maximumImagesCount : 15,
		width: options.width ? options.width : 0,
		height: options.height ? options.height : 0,
		quality: options.quality ? options.quality : 100
	};

	return cordova.exec(success, fail, "ImagePicker", "getPictures", [params]);
};

ImagePicker.prototype.getPermission = function(success, fail, options){

	return cordova.exec(success, fail, "ImagePicker", "getPermission", [{}]);
};

ImagePicker.prototype.grantPermission = function(success, fail, options){
	return cordova.exec(success, fail, "ImagePicker", "grantPermission", [{}]);
};


ImagePicker.prototype.hasOpenableAppInDevice = function(success, fail, options){
	if (!options) {
		options = {};
	}

	var params = {
		url: options.url ? options.url : ''
	};

	return cordova.exec(success, fail, "ImagePicker", "hasOpenableAppInDevice", [params]);
};

ImagePicker.prototype.getOpenableAppListInDevice = function(success, fail, options){
	if (!options) {
		options = {};
	}

	var params = {
		url: options.url ? options.url : ''
	};

	return cordova.exec(success, fail, "ImagePicker", "getOpenableAppListInDevice", [params]);
};

ImagePicker.prototype.isInstalled = function(success, fail, options){
	if (!options) {
		options = {};
	}

	var params = {
		package: options.package ? options.package : ''
	};

	return cordova.exec(success, fail, "ImagePicker", "isInstalled", [params]);
};


window.imagePicker = new ImagePicker();
