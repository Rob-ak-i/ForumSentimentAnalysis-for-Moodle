def quickFindInSortedList(element,array):
    a=0
    b=len(array)-1
    #print(element+'\n')
    latch=0
    hypothetic=0
    while(b>a):
        #x2=(int)(a+((b-a)/1.618))
        if(b-a<10 and hypothetic==0):
            recoveryCharIndexElementNumber=-1
            maxCharIndex=-1
            maxCharIndexElementNumber=-1
            for i in range(a,b+1):
                nowCharIndex=cmpstrCharIndex(element,array[i])
                if(nowCharIndex==-1):
                    return i
                if(nowCharIndex==-2):
                    if(len(element)<len(array[i])):
                        hypothetic=1
                        return i
                    elif(recoveryCharIndexElementNumber==-1):
                        recoveryCharIndexElementNumber=i
                if(maxCharIndex<nowCharIndex):
                    maxCharIndex=nowCharIndex
                    maxCharIndexElementNumber=i
            hypothetic=1
            if(len(element)<4):
                return -1
            if(recoveryCharIndexElementNumber!=-1):
                return recoveryCharIndexElementNumber
            if(maxCharIndexElementNumber==-1):
                return -1
            if(maxCharIndex>min(len(element), len(array[maxCharIndexElementNumber]))*2//3):
                return maxCharIndexElementNumber
            return -1
        if(latch==0):
            divider=(a+b)//2
            element1=array[divider]
            #print(array[a]+'!  '+element1+'  !'+array[b])
        else:
            if(latch<0):
                divider=(int)(b-((b-a)/1.618))#==x1
                element1=array[divider]
                #print(array[a]+'!'+element1+'    '+'!'+array[b])
            else:
                divider=(int)(a+((b-a)/1.618))#==x2
                element1=array[divider]
                #print(array[a]+'!'+'    '+element1+'!'+array[b])
        delta=cmpstr(element,element1)
        if(delta<0):
            latch=-1
            a=a
            b=divider
            continue
        if(delta>0):
            latch=1
            a=divider
            b=b
            continue
        #if we scan tail then we must shift to right checked tail
        #if we scan total string then we already recognized this
        if(delta==0):
            return divider
    return -1
def compareChars(c1,c2):
    return abs(c1-c2)
def compareStrings(mainstr,s1, nowSymbolIndex):
    return compareStringsInner(mainstr,s1, nowSymbolIndex,0)
def compareStringsInner(mainstr,s1, nowSymbolIndex, nowShifting):
    c=mainstr[nowSymbolIndex]
    c1=s1[nowSymbolIndex]
    if(c!=c1):
        return nowShifting,abs(c-c1)
    if(nowSymbolIndex==len(mainstr)):
        return nowShifting,0
    return compareStrings(mainstr,s1,nowSymbolIndex+1, nowShifting+1)
#выдаёт разницу между первыми несовпадающими символами
def cmpstrCharIndex(s0,s1):
    indexFinish = min(len(s0), len(s1))
    for i in range(0,indexFinish):
        c0=ord(s0[i])
        c1=ord(s1[i])
        if(c1==c0):
            continue
        return i
    if(len(s0)==len(s1)):
        return -1
    return (-2)
#выдаёт разницу между первыми несовпадающими символами
def cmpstr(s0,s1):
    indexFinish = min(len(s0), len(s1))
    for i in range(0,indexFinish):
        c0=ord(s0[i])
        c1=ord(s1[i])
        if(c1==c0):
            continue
        return c0-c1
    return (len(s0)-len(s1))
def cmptail(s0,s1, indexBegin, indexEnd):
    indexFinish = min(indexEnd, len(s0), len(s1))
    if(indexBegin<indexFinish):
        for i in range(indexBegin,indexFinish):
            c0=ord(s0[i])
            c1=ord(s1[i])
            if(c1==c0):
                continue
            return c0-c1
    return (len(s0)-len(s1))
    
    
