# Tusori-Backend

## Install
> **nginx**
```
$ sudo apt-get update
$ sudo apt-get install nginx
```

Nginx 설정 파일 수정
```
sudo vi /etc/nginx/sites-available/default
```
```
server {
    listen 80;
    server_name {탄력적 IP};

    location /spring-boot/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /fastapi/ {
        proxy_pass http://localhost:8000/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

<br/>

> **FastAPI & uvicorn**
```
$ sudo apt-get install python3-pip
$ sudo apt-get pip3 install fastapi
$ sudo apt-get pip3 install  uvicorn
```

## 그 외 [FianceDataAPI]() 참고해서 설치

> **(기타) SQL**
```
$ sudo apt-get install python3-dev default-libmysqlclient-dev build-essential
```
