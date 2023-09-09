import time

import pymongo;

client=pymongo.MongoClient("mongodb://127.0.0.1:27017/")

#x=client.list_database_names()
db=client.tracks
#print(db.list_collection_names())
cursor=db.track.find({})
for doc in cursor:
    print(doc["trackSpeed"])