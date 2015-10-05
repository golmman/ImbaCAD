@echo Creating class file
"C:\Program Files\Java\jdk1.8.0_45\bin\javac.exe" -d bin src/imbacad/util/glm.java
@echo done!
@echo(

@echo Creating C header file
"C:\Program Files\Java\jdk1.8.0_45\bin\javah.exe" -o native.h -classpath bin imbacad.util.Glm
@echo done!
@echo(

pause