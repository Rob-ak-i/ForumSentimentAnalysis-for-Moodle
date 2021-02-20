# -*- coding: utf-8 -*-
def getWordMorphParsedData(s,morpho):
    if(s.isalpha()):
        return morpho(s)[0]
    if(s.isalnum()):
        return morpho(s)[0]
        #b=list
        #b.append(s)
        #b.append()
        #return 'noun'
    return morpho(s)[0]
    #return None
def classifyWord(s):
    if(s.isalpha):
        return 2
