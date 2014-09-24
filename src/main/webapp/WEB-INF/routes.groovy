
get "/", forward: "/WEB-INF/pages/index.gtpl"
get "/datetime", forward: "/datetime.groovy"

get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"



// example routes
/*
get "/blog/@year/@month/@day/@title", forward: "/WEB-INF/groovy/blog.groovy?year=@year&month=@month&day=@day&title=@title"
get "/something", redirect: "/blog/2008/10/20/something", cache: 2.hours
get "/book/isbn/@isbn", forward: "/WEB-INF/groovy/book.groovy?isbn=@isbn", validate: { isbn ==~ /\d{9}(\d|X)/ }
*/

// routes for the blobstore service example



get "/upload",  forward: "/upload.gtpl"
get "/success", forward: "/success.gtpl"
get "/failure", forward: "/failure.gtpl"

// ws
post "/image/new", forward: "/uploadImage.groovy"

get "/image/simpletransform/@imgKey/@rez/@isCrop/", forward: "/simpleTransform.groovy?imgKey=@imgKey&rez=@rez&crop=@isCrop"

get  "/image/resize/@imgKey/@xaxis/@yaxis/", forward: "/singleTransform.groovy?op=resize&imgKey=@imgKey&xaxis=@xaxis&yaxis=@yaxis"
get  "/image/crop/@imgKey/@leftX/@topY/@rightX/@bottomY/", forward: "/singleTransform.groovy?op=crop&imgKey=@imgKey&leftX=@leftX&topY=@topY&rightX=@rightX&bottomY=@bottomY"
get  "/image/rotate/@imgKey/@degrees/", forward: "/singleTransform.groovy?op=rotate&imgKey=@imgKey&degrees=@degrees"
get  "/image/flip/@imgKey/@orientation/", forward: "/singleTransform.groovy?op=flip&imgKey=@imgKey&orientation=@orientation"
get  "/image/lucky/@imgKey/", forward: "/singleTransform.groovy?op=lucky&imgKey=@imgKey"

//no saving post and transform
post  "/stream/resize/@resizeX/@resizeY/", forward: "/streamTransform.groovy?&type=resize&resizeX=@resizeX&resizeY=@resizeY"
post  "/stream/crop/@leftX/@topY/@rightX/@bottomY/", forward: "/streamTransform.groovy?&type=crop&leftX=@leftX&topY=@topY&rightX=@rightX&bottomY=@bottomY"
post  "/stream/rotate/@rotateDegrees/", forward: "/streamTransform.groovy?&type=rotate&rotateDegrees=@rotateDegrees"
post  "/stream/verticalflip/", forward: "/streamTransform.groovy?type=verticalflip"
post  "/stream/horizontalflip/", forward: "/streamTransform.groovy?type=horizontalflip"
post  "/stream/lucky/", forward: "/streamTransform.groovy?type=lucky"

get "/image/fromUrl/@imgurl" , forward: "/imageFromURL.groovy?imgurl=@imgurl"
get "/image/@imgKey/", forward: "/serveImage.groovy?imgKey=@imgKey"
put "/image/transform", forward: "/transformImage.groovy"
put "/image/composition", forward: "/composeImage.groovy"