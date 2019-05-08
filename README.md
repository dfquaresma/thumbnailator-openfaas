# thumbnailator-openfaas
OpenFaaS function for thumbnailator experiments.

### Usage
To run the function locally you have to make sure OpenFaaS is up and running. Read the official documentation for more help. https://docs.openfaas.com/

Clone the repository:
```bash
$ git clone https://github.com/dfquaresma/thumbnailator-openfaas
```

#### Build & Deploy
```bash 
$ faas-cli up -f thumbnailator-gci.yml
```
or
```bash 
$ faas-cli up -f thumbnailator-nogci.yml
```

### Result
After deploying the OpenFaaS function `thumbnailator` will show up in the function list. You just have to hit invoke to run it. At each call, this will return the function's service time in nanoseconds.

Sample image used: https://user-images.githubusercontent.com/883386/53553708-ebb88a00-3b46-11e9-9ea8-73c6b7f9dfa1.jpg
