# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.

# Variables:
# $hostname (String) - Hostname of the website with cert error.
cert-error-intro = O servidor { $hostname } usa um certificado de segurança inválido.
cert-error-mitm-intro = Sites comprovam a própria identidade através de certificados, que são emitidos por autoridades certificadoras.
cert-error-mitm-mozilla = O { -brand-short-name } é respaldado pela Mozilla, entidade sem fins lucrativos, que administra um armazenamento de autoridades certificadoras (CA) completamente aberto. O armazenamento de autoridades certificadoras ajuda a garantir que elas estão seguindo as melhores práticas para segurança dos usuários.
cert-error-mitm-connection = O { -brand-short-name } usa o armazenamento de autoridades certificadoras da Mozilla para verificar se uma conexão é segura, em vez de certificados fornecidos pelo sistema operacional do usuário. Portanto, se um programa antivírus ou uma rede estiver interceptando uma conexão com um certificado de segurança emitido por uma autoridade certificadora que não esteja no armazenamento da Mozilla, a conexão será considerada insegura.
cert-error-trust-unknown-issuer-intro = Alguém pode estar tentando imitar o site, você não deve continuar.
# Variables:
# $hostname (String) - Hostname of the website with cert error.
cert-error-trust-unknown-issuer = Os sites comprovam sua identidade por meio de certificados. O { -brand-short-name } não confia em { $hostname } porque sua entidade certificadora é desconhecida, o certificado é autoassinado, ou o servidor não está enviando os certificados intermediários corretos.
cert-error-trust-cert-invalid = O certificado não é confiável porque foi emitido por uma entidade certificadora com certificado inválido.
cert-error-trust-untrusted-issuer = O certificado não é confiável porque o certificado do emissor não é considerado confiável.
cert-error-trust-signature-algorithm-disabled = O certificado não é confiável porque foi assinado usando um algoritmo de assinatura que foi desativado porque não é seguro.
cert-error-trust-expired-issuer = O certificado não é confiável porque o certificado do emissor está vencido.
cert-error-trust-self-signed = O certificado não é confiável porque é autoassinado.
cert-error-trust-symantec = Certificados emitidos por GeoTrust, RapidSSL, Symantec, Thawte e VeriSign não são mais considerados seguros porque essas autoridades certificadoras falharam em seguir práticas de segurança no passado.
cert-error-untrusted-default = O certificado não vem de uma origem confiável.
# Variables:
# $hostname (String) - Hostname of the website with cert error.
cert-error-domain-mismatch = Sites comprovam sua identidade através de certificados. O { -brand-short-name } não confia neste site porque ele usa um certificado que não é válido para { $hostname }.
# Variables:
# $hostname (String) - Hostname of the website with cert error.
# $alt-name (String) - Alternate domain name for which the cert is valid.
cert-error-domain-mismatch-single = Sites comprovam sua identidade através de certificados. O { -brand-short-name } não confia neste site porque ele usa um certificado que não é válido para { $hostname }. O certificado só é válido para <a data-l10n-name="domain-mismatch-link">{ $alt-name }</a>.
# Variables:
# $hostname (String) - Hostname of the website with cert error.
# $alt-name (String) - Alternate domain name for which the cert is valid.
cert-error-domain-mismatch-single-nolink = Sites comprovam sua identidade através de certificados. O { -brand-short-name } não confia neste site porque ele usa um certificado que não é válido para { $hostname }. O certificado só é válido para { $alt-name }.
# Variables:
# $subject-alt-names (String) - Alternate domain names for which the cert is valid.
cert-error-domain-mismatch-multiple = Sites comprovam sua identidade através de certificados. O { -brand-short-name } não confia neste site porque ele usa um certificado que não é válido para { $hostname }. O certificado só é válido para os seguintes nomes: { $subject-alt-names }
# Variables:
# $hostname (String) - Hostname of the website with cert error.
# $not-after-local-time (Date) - Certificate is not valid after this time.
cert-error-expired-now = Sites comprovam sua identidade através de certificados, que são válidos por um período de tempo determinado. O certificado de { $hostname } expirou em { $not-after-local-time }.
# Variables:
# $hostname (String) - Hostname of the website with cert error.
# $not-before-local-time (Date) - Certificate is not valid before this time.
cert-error-not-yet-valid-now = Sites comprovam sua identidade através de certificados, que são válidos por um período de tempo determinado. O certificado de { $hostname } será válido a partir de { $not-before-local-time }.
# Variables:
# $error (String) - NSS error code string that specifies type of cert error. e.g. unknown issuer, invalid cert, etc.
cert-error-code-prefix-link = Código do erro: <a data-l10n-name="error-code-link">{ $error }</a>
# Variables:
# $hostname (String) - Hostname of the website with cert error.
cert-error-symantec-distrust-description = Sites comprovam sua identidade através de certificados, que são emitidos por autoridades certificadoras. A maioria dos navegadores não confia mais em certificados emitidos por GeoTrust, RapidSSL, Symantec, Thawte e VeriSign. { $hostname } usa um certificado de uma dessas autoridades, portanto a identidade do site não pode ser comprovada.
cert-error-symantec-distrust-admin = Você pode notificar o administrador da página a respeito deste problema.
# Variables:
# $hasHSTS (Boolean) - Indicates whether HSTS header is present.
cert-error-details-hsts-label = HTTP Strict Transport Security: { $hasHSTS }
# Variables:
# $hasHPKP (Boolean) - Indicates whether HPKP header is present.
cert-error-details-key-pinning-label = Fixação de Chave Pública HTTP: { $hasHPKP }
cert-error-details-cert-chain-label = Cadeia de certificados:
