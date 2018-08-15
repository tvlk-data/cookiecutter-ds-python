FROM continuumio/miniconda3:latest

COPY . /app
WORKDIR /app
RUN pip install --upgrade pip
RUN pip install -r requirements.txt --ignore-installed

ENTRYPOINT ["python", "main.py"]
