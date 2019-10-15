# git 总结

## git基础

——*git安装后的配置-创建ssh

#检查电脑之前是否安装：
cd ~/.ssh

#如果有ssh key，可以直接使用，如下是如何获取 ssh key：#查看.pub文件/方法2
如果有ssh key，还可以执行备份操作：
mkdir key_backup
mv id_rsa* key_backup

#若果没有ssh 生成SSH key
ssh-keygen -t rsa -C sjy0118atsn@163.com
然后连续三个回车，不用输入密码，就可以看到成功了
说明：(这个是你github上你的账户注册时候的eamil)（下边的设置使用）

如下是生成ssh的命令行步骤：

Last login: Mon Aug 20 18:59:28 on ttys000
songjunyangdeMacBook-Pro:~ sjy$ git --version
git version 2.17.1
songjunyangdeMacBook-Pro:~ sjy$ git config --global user.name JackSong
songjunyangdeMacBook-Pro:~ sjy$ git config --global user.email sjy0118atsn@163.com
songjunyangdeMacBook-Pro:~ sjy$ cd ~/.ssh
-bash: cd: /Users/sjy/.ssh: No such file or directory
songjunyangdeMacBook-Pro:~ sjy$ ssh-keygen -t rsa -C sjy0118atsn@163.com
Generating public/private rsa key pair.
Enter file in which to save the key (/Users/sjy/.ssh/id_rsa): 
Created directory '/Users/sjy/.ssh'.
Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /Users/sjy/.ssh/id_rsa.
Your public key has been saved in /Users/sjy/.ssh/id_rsa.pub.
The key fingerprint is:
SHA256:z+lZv1YWN9A7poKxS5nsOKpuYW8YTURfsIxMTsEEq6g sjy0118atsn@163.com
The key's randomart image is:
+---[RSA 2048]----+
|   .=*....    .  |
|    *o+ o    . . |
|   ..+ +      . .|
|. .  .   .     *.|
|..  o   S *   o =|
|.  + .   X o .  o|
|E . =   + = o  o |
|   o o o + o ..  |
|  ooo.. . o  .o. |
+----[SHA256]-----+
songjunyangdeMacBook-Pro:~ sjy$ 

执行成功后，会在主目录.ssh路径下生成两个文件：id_rsa私钥文件；id_rsa.pub公钥文件；

#查看.pub文件
方法1:
cd ~/.ssh 切换目录到这个路径
vim id_rsa.pub 将这个文件的内容显示到终端上

方法2:
ls -al ~/.ssh
pbcopy < ~/.ssh/id_rsa.pub
粘贴ssh的公钥 给git服务器即可

方法3:
打开文件路径，大部分.文件都是隐藏的，使用如下命令行，可以显示.文件
defaults write com.apple.finder AppleShowAllFiles -bool true       此命令显示隐藏文件
defaults write com.apple.finder AppleShowAllFiles -bool false      此命令关闭显示隐藏文件
打开文件，sublime可以打开.pub文件，然后复制公钥给git后台



——*初始化本地仓库
(1)新建一个文件夹,cd到该目录后，使用git init即可：
cd 你的文件夹目录
git init
这时在你的目录下就会生成一个.git文件夹，不过该.git还需要配置。

(2)给.git配置一个用户名和邮箱

git config user.name JackSong
git config user.email sjy0118atsn@163.com

这时可以查看.git/config下多了你配置的这两个信息

说明：如上的配置时配置该仓库的信息，如果还有其他仓库，还需要配置，所以
想要一劳永逸，可用如下代码配置：

git config --global user.name JackSong
git config --global user.email sjy0118atsn@163.com

由于本人将Git安装在sjy目录下，所以直接查看.gitconfig文件，就有你配置的这两个信息


——*代码提交到本地仓库步骤：

#查看状态（重要）：git status

#添加新文件：git add xxx文件 多个文件用空格隔开

#提交到本地：git commit -m “这是你的提交说明/注释”

#查看修改的内容： git diff

#查看历史记录: git log

#回退上一个版本/上上个版本/上100个的版本/某一个版本：
	git reset —hard HEAD^
	git reset —hard HEAD^^
	git reset —hard HEAD～100
	git reset --hard commit_id （说明：用git log查看commit_id）

#如果ID丢了找不到了，git reflog用来查看你的每一次命令：git reflog

——*代码操作
#本地库创建一个文件：
mkdir xxx

#本地库创建一个文件：
touch 你要创建的文件，包含后缀名(eg:touch 123.java)

#本地库打开某个文件

open xxx文件包含后缀名（eg:open 123.java）

如果指定工具，则使用 -a:open －a /Application/某工具	\  你的文件
open -a /Applications/Sublime\ 123.java



——*克隆代码到本地后的一些操作：
https://blog.csdn.net/a184251289/article/details/50357941

#克隆
git clone git@192.168.10.21:RDroidUI.git

bug：The authenticity of host '192.168.10.21 (192.168.10.21)' can't be established.
ECDSA key fingerprint is SHA256:fzGsFFjRXnnhjrFR6kKiXx/iiwB4jv2iK5ltw0s78HE.
命令行执行就可以：
ssh -o StrictHostKeyChecking=no 192.168.10.21

#查看远程所有分支：
branch -a

#如何修改本地仓库地址：
克隆的整个文件拷贝到你希望的文件夹下即可

#想更换远程服务器
修改该仓库.git/config文件即可

#eclipse设置忽略文件
方式1:
设置全局忽略文件，Xcode、AS、Eclipse等，每个项目的忽略文件列表都不一样，可以统一设置忽略文件

git config --global core.excludesfile ~/.gitignore_global
vim .gitignore_global

方式2:eclipse中创建.gitignore文件
内容输入如下：

/target/
/.settings/
.classpath
.project
.gitgnore

特殊情况：eclipse有时候屏蔽.ignore文件需要设置：
view menu—>Filters—>不勾选 .*resources选项

方式3:Window——>Preferences——>Team——>Ignore Resources




## git  库操作

(1)合并某个分支的一个commit到另一个分支

首先切换到A分支

git checkout A

git log

找出要合并的commit ID :

例如

0128660c08e325d410cb845616af355c0c19c6fe

然后切换到B分支上

git checkout B

git cherry-pick  0128660c08e325d410cb845616af355c0c19c6fe

然后就将A分支的某个commit合并到了B分支了

(2) 切换分支，有unmerged提示，又想回退到前一个分支
git reset --hard HEAD

(3) 查看指定文件的历史提交记录
git log -- <file>

(4)查看每次提交的内容差异
git log -p -2 -- <file>

(5)图形化查看提交历史
gitk -- <file>

回退某次提交：

git reset --soft commitID   //只删除commitID之后的提交记录log，代码的改动还在。
 
git reset --hard commitID   //彻底删除commitID之后所做的改动，代码也一起回退回来了。 (慎重用，用前最好备份一下代码，或者用git diff 生成一个patch)


## git进阶

一上传独立分支：
1、Git init （在本地工程目录下），生成.git 文件夹
Git init
* 1
2、上传修改的文件 
git add *
* 1
(*可替换成具体要上传的文件名，*表示提交所有有变化的文件) 3、添加上传文件的描述
git commit -m "test" 
 （”test“为分支名）

4、（创建本地分支test）
git branch test

5、（切换到本地的某分支-test）
git checkout test

6.创建本地分支并切换：
git branch -b xxx

******************新建本地分支与远程分支关联:**********************
（mac版）
git checkout -b 新建分支名x
git branch --set-upstream-to=origin/分支名 本地已建分支名 （绑定远程新分支）


*****************新建本地新分支 并当作新的远程分支**************
（mac版）
git checkout -b 新建分支名x

git push --set-upstream origin 刚才新建分支名x. ( 新建分支当作远程分支)

git branch --set-upstream-to=origin/分支名 本地已建分支名 （绑定远程新分支）


*****************************************************************
删除远程分支：(需要输入账号密码)
git push  [远程名] :[分支名]       （远程名后有空格）
eg:git push origin :xxx分支

删除本地分支：
git branch -d [分支名]

批量删除本地分支：
git branch |grep xxx |xxx git branch -d


6.查看分支列表
git branch -a 
或：git branch -av

7、与远程分支相关联
git remote add origin https://github.com/yangxiaoyan20/BowlingScore.git

Git 提示fatal: remote origin already exists 错误解决办法：
1、先删除远程 Git 仓库
$ git remote rm origin
2、再添加远程 Git 仓库
$ git remote add origin git@github.com:FBing/java-code-generator
如果执行 git remote rm origin 报错的话，我们可以手动修改gitconfig文件的内容
$ vi .git/config
把 [remote “origin”] 那一行删掉就好了。

* 1
   （”BowlingScore“ 为工程名）
7、（将分支上传）
git push origin test
* 1
注意:提示 “请输入github用户名和密码“

8.返回上一次的后台提交状态 git reset --hard （+你的logid，由git log查看）

