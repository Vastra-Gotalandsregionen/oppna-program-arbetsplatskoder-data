# Build
```
docker run --rm --platform linux/amd64 -it -v ${PWD}:/client node:8 bash -c 'cd client && npm install && npm run build-prod'`
```

# Deploy
```
rsync -rv --delete -e "ssh -o StrictHostKeyChecking=no" dist/ user@server:www/apk
```
