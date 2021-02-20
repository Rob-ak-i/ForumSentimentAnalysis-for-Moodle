import os
import scripthelper as hlp
import pymorphy2
morph = pymorphy2.MorphAnalyzer(lang='ru')
nowPath = os.getcwd()
parPath = os.path.abspath(os.path.join(nowPath, os.pardir))
savesPath = os.path.abspath(os.path.join(parPath, "saves"))
fPath = os.path.abspath(os.path.join(savesPath, "words.txt"))
o1Path = os.path.abspath(os.path.join(savesPath, "words_tag.txt"))
o2Path = os.path.abspath(os.path.join(savesPath, "words_normalForm.txt"))
f = open(fPath, 'r', encoding='utf-8')
o1 = open(o1Path, 'w', encoding='utf-8')
o2 = open(o2Path, 'w', encoding='utf-8')
#for line in f:
#    if(tokens is None): tokens = tokenize1(line)
#    else: tokens+=tokenize1(line)
#parsedataarray=[]
#normwordarray=[]
words = f.readlines()
for word in words:
    word=word[:-1]
    if len(word)==0 :
        continue
    x=hlp.getWordMorphParsedData(word,morph.parse)
    o1.write(str(x[1]))
    o1.write('\n')
    o2.write(str(x[2]))
    o2.write('\n')
    #parsedataarray.append(x)
    #normwordarray.append(x[2])
f.close()
o1.close()
o2.close()

