import codecs
counter=0
label_relations = {}


def update_hm(input_line):
    if "http://www.w3.org/2000/01/rdf-schema#label" in input_line:
        input_line = input_line.replace("\n", "")
        tmp = input_line.split(" <http://www.w3.org/2000/01/rdf-schema#label> ")
        uri = tmp[0].replace("<","").replace(">","")
        label = tmp[1]
        if "@" in label:
            label = label.split("@")[0]
            label = label.replace("\"","")
        if uri in label_relations:
            labels = label_relations[uri]
            labels[label]=""
            label_relations[uri]=labels
        else:
            labels = {}
            labels[label]=""
            label_relations[uri]=labels


for line in codecs.open("labels-en-uris_es.nt","r","utf-8"):
    update_hm(line)
for line in codecs.open("labels_en.nt","r","utf-8"):
    update_hm(line)
for line in codecs.open("labels-en-uris_de.nt","r","utf-8"):
    update_hm(line)
for line in codecs.open("labels-en-uris_ja.nt","r","utf-8"):
    update_hm(line)



hm = {}
#also run this lines for infobox-properties_en.nt to get all properties with the property namespace
for line in codecs.open("mappingbased-properties_en.nt","r","utf-8"):
    line = line.replace("\n","")
    line = line.replace(" .","")
    tmp = line.split("> <")
    if len(tmp)>1:
        uri = tmp[0].replace("<","")
        property = ""
        value = ""
        if len(tmp) == 3:
            property=tmp[1].replace("<","").replace(">","")
            value = tmp[2].replace("<","").replace(">","")
        elif "> <" in tmp[1]:
            property = tmp[1].split("> <")[0]
            value = tmp[1].split("> <")[1]
        elif "> \"" in tmp[1]:
            property = tmp[1].split("> \"")[0]
            value = tmp[1].split("> \"")[1]
            value = "\""+value
            if "@" in value:
                value = value.split("@")[0]
        if "dbpedia" in property and "type" not in property and "name" not in property and "title" not in property and "isPartOf" not in property and "abstract" not in property:
            if property in hm:
                relations = hm[property]
                relations.append([uri,value])
                hm[property]=relations
            else:
                relations = []
                relations.append([uri,value])
                hm[property]=relations

print(len(hm))

for key in hm:
    try:
        name = key
        name = name.replace("http://","")
        name = name.replace(".org","")
        name = name.replace("/","_")
        #print(name)
        f_out = codecs.open("results/ontology/"+name,"w","utf-8")
        value = hm[key]
        for array in value:
            subject_uri = array[0]
            object_uri = array[1]
            if subject_uri in label_relations and object_uri in label_relations:
                subject_label = label_relations[subject_uri]
                object_label = label_relations[object_uri]
                for s in subject_label:
                    for o in object_label:
                        f_out.write(subject_uri+"\t"+s+"\t"+o+"\t"+object_uri+"\n")

            elif subject_uri in label_relations and object_uri not in label_relations:
                subject_label = label_relations[subject_uri]
                for s in subject_label:
                    if("http://dbpedia.org/resource/" in object_uri):
                        #Sometimes the resource has no label....
                        object_uri_tmp = object_uri.replace("http://dbpedia.org/resource/","")
                        object_uri_tmp = object_uri_tmp.replace("_"," ")
                        f_out.write(subject_uri+"\t"+s+"\t"+object_uri_tmp+"\t"+object_uri+"\n")
                    else:
                        f_out.write(subject_uri+"\t"+s+"\t"+object_uri+"\t"+object_uri+"\n")

            elif subject_uri not in label_relations and object_uri in label_relations:
                object_label = label_relations[object_uri]
                subject_label = subject_uri.replace("http://dbpedia.org/resource/","")
                subject_label = subject_label.replace("_"," ")
                for o in object_label:
                    f_out.write(subject_uri+"\t"+subject_label+"\t"+o+"\t"+object_uri+"\n")

            elif subject_uri not in label_relations and object_uri not in label_relations:
                subject_label = subject_uri.replace("http://dbpedia.org/resource/","")
                subject_label = subject_label.replace("_"," ")
                if("http://dbpedia.org/resource/" in object_uri):
                    #Sometimes the resource has no label....
                    object_uri_tmp = object_uri.replace("http://dbpedia.org/resource/","")
                    object_uri_tmp = object_uri_tmp.replace("_"," ")
                    f_out.write(subject_uri+"\t"+subject_label+"\t"+object_uri_tmp+"\t"+object_uri+"\n")
                else:
                    f_out.write(subject_uri+"\t"+subject_label+"\t"+object_uri+"\t"+object_uri+"\n")


        f_out.close()
    except:
        pass

print("Done")