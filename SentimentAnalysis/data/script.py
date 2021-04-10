import os
import pymorphy2
import scripthelper
morph = pymorphy2.MorphAnalyzer(lang='ru')
nowPath = os.getcwd()
#print(nowPath)
parPath=nowPath
if(nowPath.rfind("data")>=len(nowPath)-6):
    parPath = os.path.abspath(os.path.join(nowPath, os.pardir))
#print(parPath)
savesPath = os.path.abspath(os.path.join(parPath, "saves"))
RuSentiLexVocabulary = []
RuSentiLexLines = []
RuSentiLexLoaded = 0
try:
    externalPath = os.path.abspath(os.path.join(parPath, "external"))
    RuSentiLexPath = os.path.abspath(os.path.join(externalPath, "rusentilex_2017.txt"))
    #loading RuSentiLex
    RuSentiLexFile = open(RuSentiLexPath, 'r', encoding = 'cp1251')
    for line in RuSentiLexFile:
        #sentimentLoading
        if(len(line)==0):
            continue
        if(line[0]=='!'):
            continue
        #putWord
        firstCommaIndex = line.find(',')
        if(firstCommaIndex==-1):
            continue
        RuSentiLexVocabulary.append(line[0:firstCommaIndex])
        RuSentiLexLines.append(line)
    RuSentiLexLoaded = 1
    print("file external/rusentilex_2017.txt loaded, sentiment analysis mode enabled")
except Exception:
    print('file external/rusentilex_2017.txt not found - sentimentAnalysis will be disabled')
#print(savesPath)
fPath = os.path.abspath(os.path.join(savesPath, "words.txt"))
o1Path = os.path.abspath(os.path.join(savesPath, "words_tag.txt"))
o2Path = os.path.abspath(os.path.join(savesPath, "words_normalForm.txt"))
RuSentiLexResultSentimentPath = os.path.abspath(os.path.join(savesPath, "words_sentiment.txt"))
RuSentiLexResultOpinionPath = os.path.abspath(os.path.join(savesPath, "words_opinion.txt"))
RuSentiLexResultMeanPath = os.path.abspath(os.path.join(savesPath, "words_mean.txt"))
f = open(fPath, 'r', encoding='utf-8')
o1 = open(o1Path, 'w', encoding='utf-8')
o2 = open(o2Path, 'w', encoding='utf-8')
if(RuSentiLexLoaded):
    RuSentiLexResultSentiment = open(RuSentiLexResultSentimentPath, 'w', encoding='utf-8')
    RuSentiLexResultOpinion = open(RuSentiLexResultOpinionPath, 'w', encoding='utf-8')
    RuSentiLexResultMean = open(RuSentiLexResultMeanPath, 'w', encoding='utf-8')
#for line in f:
#    if(tokens is None): tokens = tokenize1(line)
#    else: tokens+=tokenize1(line)
#parsedataarray=[]
#normwordarray=[]
words = f.readlines()
#i=0
word1=""
for word in words:
    #i=i+1
    #if(i==500):
    #    break
    word=word[:-1]
    if len(word)==0 :
        o1.write('\n')
        o2.write('\n')
        continue
    tags=morph.parse(word)
    x=tags[0]
    #o1.write(str(x[1]))
    #o1.write(';')
    o1.write(str(x.tag.POS))
    o1.write(',')
    o1.write(str(x.tag.animacy))
    o1.write(',')
    o1.write(str(x.tag.aspect))
    o1.write(',')
    o1.write(str(x.tag.case))
    o1.write(',')
    o1.write(str(x.tag.gender))
    o1.write(',')
    o1.write(str(x.tag.involvement))
    o1.write(',')
    o1.write(str(x.tag.mood))
    o1.write(',')
    o1.write(str(x.tag.number))
    o1.write(',')
    o1.write(str(x.tag.person))
    o1.write(',')
    o1.write(str(x.tag.tense))
    o1.write(',')
    o1.write(str(x.tag.transitivity))
    o1.write(',')
    o1.write(str(x.tag.voice))
    o1.write('\n')
    o2.write(str(x[2]))
    o2.write('\n')
    #parsedataarray.append(x)
    #normwordarray.append(x[2])
    if(RuSentiLexLoaded==0):
        continue
    if(len(x[2])<4):
        index=-1
    else:
        index=scripthelper.quickFindInSortedList(x[2],RuSentiLexVocabulary)
    if(index==-1):
        RuSentiLexResultOpinion.write('\n')
        RuSentiLexResultSentiment.write('\n')
        continue
    word1=RuSentiLexLines[index]
    commaIndex=-1;
    nextCommaIndex=-1;
    commaIndex=word1.find('\"',0,-1)#if(word1[-1]=='\"'):
    #    commaIndex=word1.rfind('\"',0,-2)
    #    commaIndex=word1.rfind(',', 0,commaIndex)
    nextCommaIndex=word1.rfind(',', 0,commaIndex)
    RuSentiLexResultMean.write(RuSentiLexVocabulary[index]+'\n')
    if(scripthelper.cmpstrCharIndex(x[2],RuSentiLexVocabulary[index])!=-1):
        RuSentiLexResultOpinion.write('hypothetic'+'\n')
    else:
        RuSentiLexResultOpinion.write(word1[nextCommaIndex+2: commaIndex]+'\n')
    commaIndex=word1.rfind(',',0,nextCommaIndex)
    RuSentiLexResultSentiment.write(word1[commaIndex+2: nextCommaIndex]+'\n')
f.close()
o1.close()
o2.close()
if(RuSentiLexLoaded):
    RuSentiLexResultSentiment.close()
    RuSentiLexResultOpinion.close()
    RuSentiLexResultMean.close()
print("done!")
