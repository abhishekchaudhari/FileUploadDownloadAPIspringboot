# FileUploadDownloadAPIspringboot

PROJECT CURLs
UPLOAD:
curl --location --request POST 'http://localhost:8080/api/files/upload' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--form 'file=@"/C:/Users/ABHISHEK/Downloads/CodingTuly Title (3).jpg"' \
--form 'passcode="hello"'

DOWNLOAD:(YOU WILL GET THIS CURL IN RESPONSE AS WELL)
curl --location --request GET 'http://localhost:8080/api/files/download/ad4ae623-b0ca-436c-9254-5b8ec423f6e1?passcode='

NOTE: /target folder uploaded where executable jar file is availiable