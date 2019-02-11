import http.client
import json
import urllib
import base64

import googleapiclient.discovery
from google.cloud import storage
from retrying import retry

def retry_if_exception(exception):
    print("Got some exception during HTTP request from rm-sdk. Retrying..")
    return True

def parse_uri(uri):
    """Parse an GCS URI, returning (bucket, path)"""
    parsed = urllib.parse.urlparse(uri)
    if parsed.scheme != "gs":
        raise Exception("Not a GCS URI: %s" % uri)
    path = parsed.path
    if path.startswith('/'):
        path = path[1:]
    return parsed.netloc, path

@retry(retry_on_exception=retry_if_exception, stop_max_attempt_number=3, wait_random_min=1000, wait_random_max=2000)
def get_auth0_config(auth0_config_uri, project_id='tvlk-data-mlplatform-prod',location_id='global',
                key_ring_id='raring-meerkat-common', crypto_key_id='auth0-dev'):
    bucket_name, filePath = parse_uri(auth0_config_uri)
    storage_client = storage.Client()
    bucket = storage_client.get_bucket(bucket_name)
    blob = bucket.blob(filePath)
    kms_client = googleapiclient.discovery.build('cloudkms', 'v1', cache_discovery=False)
    name = 'projects/{}/locations/{}/keyRings/{}/cryptoKeys/{}'.format(
        project_id, location_id, key_ring_id, crypto_key_id)
    ciphertext = blob.download_as_string()
    crypto_keys = kms_client.projects().locations().keyRings().cryptoKeys()
    request = crypto_keys.decrypt(
        name=name,
        body={'ciphertext': base64.b64encode(ciphertext).decode('ascii')})
    response = request.execute()
    plaintext = base64.b64decode(response['plaintext'].encode('ascii'))
    return json.loads(plaintext)