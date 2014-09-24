import com.google.appengine.api.images.Image
import com.google.appengine.api.images.ImagesServiceFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.fileupload.util.Streams
import org.apache.commons.io.IOUtils
import com.xaymaca.appengine.GroovyTransformer

/* gets image upload. From Mr. Haki's blog - http://mrhaki.blogspot.com/2009/11/add-file-upload-support-to-groovlets-in.html */
uploads = [:]  // Store result from multipart content.
if (ServletFileUpload.isMultipartContent(request)) {

    log.info "made it here!"
    def uploader = new ServletFileUpload()  // Cannot use diskbased fileupload in Google App Engine!
    def items = uploader.getItemIterator(request)
    while (items.hasNext()) {
        def item = items.next()
        def stream = item.openStream()
        try {
            if (item.formField) {  // 'Normal' form field.
                params[item.fieldName] = Streams.asString(stream)
            } else {
                uploads[item.fieldName] = [
                    name: item.name,
                    contentType: item.contentType,
                    data: IOUtils.toByteArray(stream)
                ]
            }
        } finally {
            IOUtils.closeQuietly stream
        }
    }

    log.info "we have params: "  + params
    //ok, we now have our uploaded image
 
    
    if (params.type != null) {

      log.info "params "  + params

        Map args = params

      def type = params.type
      def responseXML
        // Gaelyk provides a shortcut to the image service, but all the heavy lifting is done by the ImagesServiceFactory.

        // get original image
        Image pic = ImagesServiceFactory.makeImage( uploads['photo'].data )


        //byte[] binImage   = pic.imageData    //TODO not used ?
            

        log.info "the format " + pic.getFormat()
        String imgType = pic.getFormat()
        def ext = imgType.toLowerCase()
        def mime = "image/" + ext
        log.info "mime is " + mime

        //use transform class to return downloadible image


        def transformer = GroovyTransformer.getInstance()

        def newImage = transformer.singleTranform(type,args,pic) ;



         // imgType = info.getContentType()
    //log.info "content type is " + imgType  + " broken down: " + imgType.toLowerCase().split("/")[1]
    //response.setContentType(imgType.toLowerCase())
    response.setContentType(  mime )
    response.setHeader("Content-Disposition","inline;filename=image.${ext}")


    sout << newImage.imageData
 
       

    }
    
    else {


    response.setStatus(500)
    def erroResponseXML = """<?xml version="1.0" encoding="UTF-8"?>
    <errors>
    <error type="missing parameters"/>
    <error message="please include required parameters"/>
    </errors>
    """
    out << erroResponseXML
        
    }
}
else {
    response.setStatus(500)
    def erroResponseXML = """<?xml version="1.0" encoding="UTF-8"?>
    <errors>
    <error type="bad ContentType"/>
    <error message="wrong content type, try multipart instead"/>
    </errors>
    """
    out << erroResponseXML
}
/* end upload processing */

