@echo Creating class file
"C:\Program Files\Java\jdk1.8.0_45\bin\javac.exe" -d bin src/imbacad/model/ocr/Test.java
@echo done!
@echo(

@echo Creating C header file
"C:\Program Files\Java\jdk1.8.0_45\bin\javah.exe" -o tess.h -classpath bin imbacad.model.ocr.Test
@echo done!
@echo(

pause