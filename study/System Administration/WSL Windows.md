Si ya tienes terminal en windows entonces correr este comando, puedes cambiar la distribución, para saber las distribuciones quita la -d y solo deja --install
```terminal
wsl --install -d Ubuntu-24.04
```
Para saber que versiones hay
```terminal
wsl --list -o
```


Instalar ssh en un servidor linux:
```terminal
sudo apt-get update && sudo apt-get install -y openssh-server
```

Asegurarse de que el servicio esté arrancado con:
```terminal
sudo service ssh start
```

Editar el archivo de configuración con 
```terminal
sudo vim /etc/ssh/sshd_config 
```
o con cualquier otro editor de texto. El fichero debe contener al menos las siguientes opciones. Estas opciones no son las más seguras, pero servirán para poder hacer pruebas en entornos locales: 
```
• Port 22. 
• ListenAddress 0.0.0.0. 
• PasswordAuthentication yes.
```

Recargar la configuración con
```terminal
sudo service sshd reload
```

Averiguar la IP (internet protocol) del equipo con:
```terminal
ip address
```

En este punto, ya sería posible usar un cliente para conectarse y, en un entorno local, no será necesario más configuración. En un entorno de nube, será necesario haber habilitado el puerto 22 en el grupo de seguridad de la instancia.

Asumiendo que el servidor SSH ha sido configurado correctamente, se podría usar el cliente SSH desde un equipo Linux o MacOS con SSH <nombre_usuario>@<ip_servidor>. Ejemplo:
```terminal
ssh ubuntu@192.168.1.130
```

![[Pasted image 20241019213911.png]]
Para acceder desde un equipo Windows, se podría usar Putty. Es el cliente SSH recomendado para entornos Windows, está disponible para su descarga y es gratuito. Tras ejecutar Putty, se presentará la primera pantalla de configuración.
![[Pasted image 20241019213938.png]]

En esta pantalla, será posible configurar la información para memorizar diferentes sesiones de conexión. En el caso más sencillo, será necesario marcar connection type a SSH e introducir la IP en el campo Host Name (o IP address). Tras presionar el botón Open,se abrirá una ventana de terminal donde se solicitará el usuario y la contraseña para intentar el inicio de sesión. En el primer intento de conexión, aparecerá una advertencia que el equipo es desconocido. Una vez aceptado este aviso, no volverá a aparecer, a menos que se use el mismo nombre de equipo o la misma IP para conectarse a un equipo diferente.

