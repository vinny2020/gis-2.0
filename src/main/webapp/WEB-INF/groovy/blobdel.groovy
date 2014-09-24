import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobInfoFactory
/**
 * Created by IntelliJ IDEA.
 * User: xaymaca
 * Date: 2/21/11
 * Time: 3:17 PM
  to be used in a cron job to delete older blobs
 */
List<BlobInfo> blobsToDelete = new LinkedList<BlobInfo>();
Iterator<BlobInfo> iterator = new BlobInfoFactory().queryBlobInfos();
while(iterator.hasNext())
blobsToDelete.add(iterator.next());

BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();


blobsToDelete.each {BlobInfo blobInfo -> blobstoreService.delete(blobInfo.getBlobKey()) };