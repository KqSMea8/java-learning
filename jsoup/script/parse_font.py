# -*- coding:utf-8 -*-
import sys
print "python脚本传参: ",sys.argv[0]
font_file = sys.argv[0]
# 列出已下载文件
file_list = os.listdir('./fonts')
# 判断是否已下载
if font_file not in file_list:
    # 未下载则下载新库
    print('不在字体库中, 下载:', font_file)
    url = 'http://vfile.meituan.net/colorstone/' + font_file
    new_file = self.get_html(url)
    with open('./fonts/' + font_file, 'wb') as f:
        f.write(new_file)
font = TTFont('./font/' + font_file)
font.saveXML('./font/' + font_file + '.xml')
