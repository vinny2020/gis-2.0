 reader = new BufferedReader(new InputStreamReader(request.getInputStream()));

  


 def  fileText   = reader.text

 fileText.eachLine  {line, i ->

   if(i > 2) {
     println "line" + i + ": " + line  + "<br/>"
   }


 }

