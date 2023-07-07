<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
  <title>選擇文件插入圖片</title>
  <script>
    function showImage(event) {
      var input = event.target;
      var reader = new FileReader();
      
      reader.onload = function(){
        var img = document.getElementById('image');
        img.src = reader.result;
      };
      
      reader.readAsDataURL(input.files[0]);
    }
  </script>
</head>
<body>
  <input type="file" accept="image/*" onchange="showImage(event)" />
  <br>
  <img id="image" src="#" alt="Selected Image" />
</body>
</html>
