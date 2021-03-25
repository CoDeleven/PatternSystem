

# 上亿花样系统

## 概念
* 花样机原点：花样机有自己固定的原定，一般都在板子的中心点，在软件上即为 (0, 0)
* 次元点：人为设置的原点，和空送有点类似，但是次元点还有“记忆”能力，使得下一次车缝直接从次元点开始，省去了再次空送的时间
* 空送：就是不打直接挪动
* 一个数据文件除开最后的结束标志位，最后一帧肯定是回到原点
* 每一帧的X、Y都是相对上一帧的偏移量
* 如果是闭合图形且最后一帧回到原点那么就不会再做空送了

## 文件结构
* 0x10 针迹部分占用的总字节数（即0x2C起的 帧有几个，若有10个，则此处为10 * 4 =40(0x28) ）
* 0x14 从第一帧开始算起到结尾有几帧（四个字节为一帧）
* 0x16 总帧数（每帧4个字节）
* 0x18 最左边的点的X （负数的话是 最小值(负数) + 除最高位以外的值，即溢出）（2个字节）
* 0x1A 最靠右边的点的X（2个字节）
* 0x1C 最低点的Y（2个字节）
* 0x1E 最高的点的Y（2个字节）
* 0x2C 针迹点开始的位置，每一帧的数据（4个字节）

### 帧结构
上亿里，一针占用4个字节：
```txt
0x61 0x00 0x17 0x0B
```
* `0x61` 表示 控制码
* `0x00` 目前不知道干啥用       (功能码暂停压框落下 或这 暂停压框抬起，主要控制抬起还是落下。抬起为1，落下为0)
* `0x17` 表示相对上一帧X的距离
* `0x0B` 表示相对上一帧Y的距离

## 控制码

* `0x61` 车缝+高速
* `0x1B` 空送
* `0x03` 一次移动若超过 +127 ~ -127 那么中间需要用0x03填充
* `0x41` 车缝+中高
* `0x21` 车缝+中低
* `0x01` 车缝+低速
* `0x02` 剪线（通过点击针迹选择剪线码设置的）。注意，加了这个功能码之后的一个点是`0x02 0x0 0x0 0x0` (如果通过增加空送来添加剪线码，似乎会在空送针前后进行添加，很奇怪)
* `0x07` 如果线拉的很长就会有这个（单次移动的距离超过127）。猜测这个控制码是落针的意思，因为持续移动(0x3)后，会有一个`0x07`，似乎是为了落针

## 功能码
* `0x04` 上暂停压 压下

## BUG
* 上亿系统的BUG，如果最小的Y大于0，那么minY就会等于0。猜测是 运行时添加了一个(0,0)到集合里（导出的时候会移除）。
* （在[0,0]增加一个节点保存后再打开发现[0,0]的点被移除了，所以觉得会添加一个(0,0)到集合里且导出的时候会移除）


# 大豪花样系统

## 文件结构
大豪的文件结构分为三层：
1. 文件头，描述整个文件的信息
2. 构造线信息
3. 缝纫机信息

### 文件头
1. `6E 73 70 00 80 00 00 00`，暂且认为是文件头吧
2. `50 01 00 00 00 00 00 00`，其中`50 01` 表示有用的机器码字节数
3. `C0 00 00 00 00 00 00 00`，其中`C0 00` 表示机器码从哪个位置开始读起
4. `00 00 19 00 00 00 BE 02`，好像固定是这玩意
5. 然后是 48 个字节的空行，也就是3行 16列都为0，标识结束



### 构造线信息
里面每一个图形（按照剪线划分）称为一个对象，一个对象有一个自己的描述符，构造线描述符的第一行往往保存着 机器格式信息、起点信息等等
```text
0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F
88 00 00 00 46 00 01 00 54 00 B2 FF 00 00 00 00
```
* `88 00 00 00 46 00` 目前不知道什么含义，但是一个图形开头必须得加上
* `01 00` 表示跳针数量
* `00 54` 表示 构造线起点的X偏移，`B2 FF` 表示 构造线起点的Y偏移
* `00 00` 目前没发现用处，默认都给0

```text
0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F
C8 00 60 00 1E 00 0A 00 E0 FF 20 00 00 00 00 00
```
* `CB 00 60 00 1E 00` 目前不知道什么含义，但是一个图形结尾必须加上
* `0A 00` 这一条线包含的点的个数
* `E0 FF` 表示这个点相对前一个点的X偏移
* `20 00` 表示这个点相对前一个点的Y偏移
* `00 00` 目前没有发现用处，默认都给0

* 注意，遇到这种基本就是对一个图形的描述：比如直线
* 如果以 `D8 02 60 00 1E 00` 开头，也同样表示是一条直线，其中`DB` 后的 `02` 表示这条线段还有后续，若没有 `02` 这个线段结束后直接显示 `END` 
* `C8 01 60 00 1E 00 0A 00 C6 FF DC FF 00 00 00 00` 其中`C8 01` 表示 三点圆弧针迹
* `CC 01 60 00 1E 00 0A 00 0A 00 DC FF 00 00 00 00` 其中`CC 01` 表示 圆针迹

```text
0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F
01 02 00 00 00 00 01 00 00 00 00 00 00 00 00 00
```
表示终点的意思（怀疑是剪线的意思），里面的`01 02` 是功能码，后面的`01` 表示就1个点。

```text
0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F
01 1F 00 00 00 00 00 00 00 00 00 00 00 00 00 00
```
表示构造线层信息已经结束

### 机器码
机器码参照上亿系统的