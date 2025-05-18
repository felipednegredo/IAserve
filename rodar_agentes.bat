@echo off
setlocal

:: Caminho do JDK
set JAVA_HOME=C:\Users\User\.jdks\openjdk-24.0.1

:: Caminho COMPLETO do jade.jar
set JADE_JAR="C:\Users\User\IdeaProjects\IAserve\lib\JADE\JADE-bin-4.6.0\jade\lib\jade.jar"

:: Cria pasta de saída se não existir
if not exist out mkdir out

:: Compilar os arquivos Java
echo Compilando...
"%JAVA_HOME%\bin\javac.exe" -cp %JADE_JAR% -d out src\main\MainJADE.java src\agentes\Chef.java src\agentes\Garcom.java src\agentes\Cliente.java src\agentes\Gerente.java

if errorlevel 1 (
    echo ERRO NA COMPILACAO.
    pause
    exit /b
)

:: Rodar JADE com os agentes
echo Executando agentes com JADE...
"%JAVA_HOME%\bin\java.exe" -cp "out;%JADE_JAR%" jade.Boot -gui gerente:agentes.Gerente garcom:agentes.Garcom cliente:agentes.Cliente chef:agentes.Chef

endlocal
pause
