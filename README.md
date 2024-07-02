# Tusori-Backend

## Install
> **nginx**
```
$ sudo apt-get update
$ sudo apt-get install nginx
```

Nginx 설정 파일 수정
```
sudo vi /etc/nginx/sites-available/tusori.conf
sudo ln -s /etc/nginx/sites-available/tusori.conf /etc/nginx/sites-enabled/tusori.conf
```
```
server {
    listen 80;
    server_name {탄력적 IP};

    location /springboot/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # CORS 관련 헤더 설정
        add_header 'Access-Control-Allow-Origin' '{탄력적 IP}' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' '*' always;
        add_header 'Access-Control-Allow-Credentials' 'true' always;

        # Timeout 설정 추가
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
        keepalive_timeout 60s;
    }

    location /springboot/notification/ {
        proxy_pass http://localhost:8080/notification/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        proxy_buffering off;
        proxy_cache off;

        # SSE용 Timeout 설정
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 3600s;
        keepalive_timeout 60s;
    }

    location /fastapi/ {
        proxy_pass http://localhost:8000/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # CORS 관련 헤더 설정
        add_header 'Access-Control-Allow-Origin' '{탄력적 IP}' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' '*' always;
        add_header 'Access-Control-Allow-Credentials' 'true' always;

        # Timeout 설정 추가
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
        keepalive_timeout 60s;
    }

    error_log /var/log/nginx/error.log;
    access_log /var/log/nginx/access.log;
}
```
```
sudo nginx -t
sudo systemctl reload nginx
sudo systemctl restart nginx
```

<br/>

> **FastAPI & uvicorn**
```
$ sudo apt-get install python3-pip
$ sudo apt-get pip3 install fastapi
$ sudo apt-get pip3 install uvicorn
```

## 그 외 [FianceDataAPI](https://github.com/Tu-Sori/FinanceDataAPI/blob/main/README.md) 참고해서 설치

> **(기타) SQL**
```
$ sudo apt-get install python3-dev default-libmysqlclient-dev build-essential
```
