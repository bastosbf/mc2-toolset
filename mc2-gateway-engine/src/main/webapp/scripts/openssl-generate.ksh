#!/bin/ksh

# Script para gera��o da chave privada e do certificado digital para
# conex�o com o OpenBus.
# 
# $Id$

which openssl > /dev/null 2>&1 || {
  print "comando 'openssl' nao foi encontrado"
  exit 1
}

scriptName=$(basename $0)

function usage {
    cat << EOF

Uso: $scriptName [opcoes]

  onde [opcoes] sao:

  -h      : ajuda
  -c arq  : arquivo de configuracao do OpenSSL
  -n nome : nome do arquivo com o certificado

OBS.: se o nome nao for fornecido via '-n' sera obtido interativamente
EOF
}

while getopts "hc:n:" params; do
     case $params in
        h)
            usage
            exit 0
        ;;
        c)
            sslConfig="-config $OPTARG"
        ;;
        n)
            entityName="$OPTARG"
        ;;
        *)
            usage
            exit 1
        ;;
     esac
done

# descartamos os parametros processados
shift $((OPTIND - 1))

if [ -z "$entityName" ]; then
  echo -n "Nome da chave: "
  read entityName
fi

# se o usu�rio n�o especificou um arquivo de configura��o para o
# OpenSSL e a vari�vel OPENBUS_HOME est� definida, usamos o arquivo de
# configura��o do OpenSSL distribu�do com o OpenBus
if [ -z "$sslConfig" ]; then
    if [ -n "${OPENBUS_HOME}" ]; then
        # se OPENBUS_HOME est� definida, podemos assumir que
        # OPENSSL_HOME tamb�m est�
        sslConfig="-config ${OPENSSL_HOME}/openssl.cnf"
    fi
fi

print "### Criando certificados para o Openubs ###\n"

openssl genrsa -out ${entityName}_openssl.key 2048
openssl pkcs8 -topk8 -nocrypt -in ${entityName}_openssl.key -out ${entityName}.key -outform DER
openssl req -new -x509 -key ${entityName}.key -keyform DER -out ${entityName}.crt -outform DER

rm -f ${entityName}_openssl.key

print "\nChave privada : ${entityName}.key"
print "Certificado   : ${entityName}.crt"
