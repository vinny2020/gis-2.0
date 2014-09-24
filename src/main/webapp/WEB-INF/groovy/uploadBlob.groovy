def blobs = blobstore.getUploadedBlobs(request)
def blobKey = blobs["george"]

//response.status = 302

if (blobKey) {
   // log.info "got an image object!!!!!"
  response.setHeader("myKey",blobKey.keyString)
	//redirect "/success?key=${blobKey.keyString}"	
} else {
    log.info "fail, epic"
	//redirect "/failure"
}
