@echo Creating class file
"C:\Program Files\Java\jdk1.8.0_45\bin\javac.exe" -d bin src/imbacad/model/dwg/DWG.java
@echo done!
@echo(

@echo Creating C header file
"C:\Program Files\Java\jdk1.8.0_45\bin\javah.exe" -o jdwg.h -classpath bin imbacad.model.dwg.DWG
@echo done!
@echo(

pause