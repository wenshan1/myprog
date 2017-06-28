# -*- coding: utf-8 -*-
"""
Created on Wed Jun 28 17:28:01 2017

@author: blan
"""

'''
采用 tushare 模块来采集指数和股票K线数据
'''
import tushare as ts
import pymysql.cursors
from datetime import datetime, timedelta

data_frame = 0
connect = pymysql.Connect(  
        host='localhost',  
        port=3306,  
        user='root',  
        passwd='root',  
        db='stock',  
        charset='utf8'  
        )  

def GetIndexData (stockName, bInit = False):
    global data_frame
    if (bInit):
        data_frame = ts.get_k_data(stockName, start='2004-01-01',  index = True)        
    else:
         now = datetime.now()
         start = now - timedelta(days=1)
         startStr = start.strftime ('%Y-%m-%d')
         data_frame = ts.get_k_data(stockName, start=startStr,  index = True)        
         
    nCount = 0
    cursor = 0
    insert_sql = "INSERT INTO stockindex (name, riqi, volume, openprice, closeprice, highprice, lowprice, adjcloseprice) VALUES ('%s', '%s', %s, %s, %s, %s, %s, %s);"
    for index, row in data_frame.iterrows():
        # 这里修改为组装SQL并执行的语句
        print (row['code'],row['date'], row ['volume'])
        
        if nCount == 0:
            cursor = connect.cursor() 
            
        row_tulpe = (row['code'],row['date'],row ['volume'],row['open'],row['close'],row['high'],row['low'], '0.0')
        wsql = insert_sql % row_tulpe
        
        try:
            cursor.execute(wsql) 
            nCount = nCount + 1
            
            if nCount >= 500 or bInit :
                connect.commit()
                cursor.close()
                nCount = 0
        except Exception as e:
            pass
                
    if nCount < 100:
        connect.commit() 
        cursor.close ()            


#初始化指数数据

def getIndexData (bInit = False):
    stockIndexs = ('000001', '399001', '000300', '000905', '399006', '399005', '000010', '000016')
    for stock in stockIndexs:
        GetIndexData (stock, bInit)
    

if __name__ == '__main__':
    
    getIndexData (False)
    connect.close ()
