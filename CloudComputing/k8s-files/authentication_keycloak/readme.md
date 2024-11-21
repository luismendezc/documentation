Intructions
first create the certificate with the below command:

openssl req -x509 -out localhostcert.pem -keyout localhostkey.pem \
 -newkey rsa:2048 -nodes -sha256 \
 -subj '/CN=localhost' -extensions EXT -config <( \
 printf "[dn]\nCN=localhost\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:localhost,DNS:internal.oceloti.com,IP:10.151.130.198\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")

copy the 2 generated files to the certs folder

run the command:
make up
