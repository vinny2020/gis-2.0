import org.apache.commons.io.IOUtils
import org.apache.commons.fileupload.util.Streams
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
//import com.xaymaca.appengine.Hello

import com.xaymaca.appengine.BlobCreator
import com.xaymaca.appengine.Persister

/* gets image upload. From Mr. Haki's blog - http://mrhaki.blogspot.com/2009/11/add-file-upload-support-to-groovlets-in.html */
uploads = [:]  // Store result from multipart content.
if (ServletFileUpload.isMultipartContent(request)) {
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


    if (params.submit) {

        log.info "params " + params
        def responseXML
        // Gaelyk provides a shortcut to the image service, but all the heavy lifting is done by the ImagesServiceFactory.

        // get original image
        Image pic = ImagesServiceFactory.makeImage(uploads['photo'].data)
        def uuid

        byte[] binImage = pic.imageData

        def creator = new BlobCreator()
        log.info "the format " + pic.getFormat()
        String imgType = pic.getFormat()
        def mime = "image/" + imgType.toLowerCase()
        log.info "mime is " + mime

        def keymap = creator.addBlob("new", "george", binImage, mime)
        if (keymap != null) {
           // log.info "we are good.  " + " new key " + keymap.getClass()

            if (!keymap.get("cached")) {
                Persister p = new Persister(keymap.get("blobKey"))
                uuid = p.persist()
            }
            // set response type







                response.setContentType("application/xml")
                responseXML = """<?xml version="1.0" encoding="UTF-8"?>
<image>
<key>${uuid}</key>
<fileURL>/image/${uuid}/</fileURL>
</image>
"""


            // render image out
            //out << pic.imageData


            out << responseXML


        }

        else {
            response.status = 500
            StringBuilder errorXml = new StringBuilder();
            sb.append("<?xml version=\" 1.0 \" encoding=\" UTF - 8 \"?>");
            sb.append("<errors>\n");
            sb.append("<error type=\"timeout\">\n");
            sb.append("<error message=\"Timed Out, URLFetch exceeded 10 seconds\">\n");
            sb.append("<errors>\n");

            out << errorXml.toString()

        }


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

