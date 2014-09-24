
/**
 * Created by IntelliJ IDEA.
 * User: xaymaca
 * Date: 2/13/11
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.appengine.api.datastore.KeyFactory
import com.google.appengine.api.datastore.Entity;



def key = KeyFactory.createKey("myids", "ido") ;



println "key is " + key.getId();

 def imageIDs = new Entity("newid")

    imageIDs.shortID = key.getId()
    imageIDs.save()

def letters = "a".."z";

log.info letters.getClass().toString()


println " this should be a " + letters.get(0) + "<br/>";
println " this should be z " + letters.get(25) + "<br/>";

def random = new Random()

def random1 = random.nextInt(letters.size())
def random2 = random.nextInt(letters.size())
println letters.get(random1) + letters.get(random2);