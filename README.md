## 기본환경 세팅

* docker run

```sh
docker run --name jw-cache-redis -d -p 6379:6379 redis:8.2.1
```

* redis cli

```sh
docker exec -it 9dd537abac6a redis-cli
```

