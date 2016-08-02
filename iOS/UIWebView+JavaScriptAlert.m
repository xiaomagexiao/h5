//
//  UIWebView+JavaScriptAlert.m
//  mooc
//
//  Created by 网梯 on 15/11/27.
//  Copyright © 2015年 whaty . All rights reserved.
//

#import "UIWebView+JavaScriptAlert.h"
@implementation UIWebView (JavaScriptAlert)

static BOOL status = NO;
static BOOL isEnd =NO;


static NSInteger newIndex = 0;

-(void)webView:(UIWebView *)sender runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(id)frame{

    UIAlertView* dialogue = [[UIAlertView alloc]initWithTitle:nil message:message delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [dialogue show];
}


- (NSString *) webView:(UIWebView *)view runJavaScriptTextInputPanelWithPrompt:(NSString *)prompt defaultText:(NSString *)text initiatedByFrame:(id)frame {
    NSLog(@"prompt-->%@",prompt);
    NSDictionary * dict = [self dictionaryWithJsonString:prompt];
    NSLog(@"dict-->%@",dict);
    NSString *string = dict[@"command"];
    NSLog(@"string-->%@",string);
    if ([string isEqualToString:@"userInfo"]) {
        NSLog(@"返回的json--->%@",[NSString stringWithFormat:@"{\"loginType\":\"%@\"}",[[NSUserDefaults standardUserDefaults] objectForKey:@"Def_loginType"]]);
        return [NSString stringWithFormat:@"{\"loginType\":\"%@\"}",[[NSUserDefaults standardUserDefaults] objectForKey:@"Def_loginType"]];
//        return @"{\"loginType\":\"d714203aadde495f8b0e59da37779835\"}";
    }
    else if([string isEqualToString:@"openview"]){
        NSString *link = dict[@"link"];
        NSString *title = dict[@"title"];
        NSLog(@"link-->%@",link);
        NSLog(@"title-->%@",title);
        [[NSNotificationCenter defaultCenter] postNotificationName:@"openview" object:dict];
        return nil;
    }
    else if([string isEqualToString:@"opencourse"]){
        NSString *courseId = dict[@"courseId"];
        NSString *imageUrl = dict[@"imageUrl"];
        NSLog(@"courseId-->%@",courseId);
        NSLog(@"imageUrl-->%@",imageUrl);
        [[NSNotificationCenter defaultCenter] postNotificationName:@"opencourse" object:dict];
        return nil;
    }
    
    NSArray *array = [string componentsSeparatedByString:@"-"];
    //根据command判断操作类型
    UIActionSheet * sheet = [[UIActionSheet alloc] initWithTitle:dict[@"command"] delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:nil otherButtonTitles:array[0],array[1],array[2],array[3], nil];
    [sheet showInView:self];
    
    while (isEnd ==NO) {
        [[NSRunLoop mainRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:0.01f]];
    }
    isEnd = NO;
    return [NSString stringWithFormat:@"{\"result\":\"%zi\"}",newIndex];
}
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    newIndex = buttonIndex;
    isEnd = YES;
}


- (BOOL)webView:(UIWebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(id)frame {
    UIAlertView *confirmDiag = [[UIAlertView alloc] initWithTitle:nil
                                                          message:message
                                                         delegate:self
                                                cancelButtonTitle:@"取消"
                                                otherButtonTitles:@"确定",nil];
    
    [confirmDiag show];
    
    while (isEnd ==NO) {
        [[NSRunLoop mainRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:0.01f]];
    }
    isEnd = NO;
    return status;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    status = buttonIndex;
    isEnd = YES;
}


/*
 -(BOOL)webView:(UIWebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(id)frame{
 //    UIAlertController * alertVC = [UIAlertController alertControllerWithTitle:@"提示信息" message:message preferredStyle:UIAlertControllerStyleAlert];
 //
 //    UIAlertAction *completeAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"确定", @"Okay") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
 //        diagStat=YES;
 //    }];
 //    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"取消", @"Cancel") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
 //        diagStat=NO;
 //    }];
 //    [alertVC addAction:completeAction];
 //    [alertVC addAction:cancelAction];
 //    [self.window.rootViewController  presentViewController:alertVC animated:YES completion:nil];
 //
 //
 UIAlertView* dialogue = [[UIAlertView alloc] initWithTitle:nil message:message delegate:self cancelButtonTitle:NSLocalizedString(@"ok", @"Ok") otherButtonTitles:NSLocalizedString(@"Cancel", @"Cancel"), nil];
 [dialogue show];
 while (dialogue.hidden==NO && dialogue.superview!=nil) {
 [[NSRunLoop mainRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:0.01f]];
 }
 
 return diagStat;
 }
 
 -(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
 if (buttonIndex==0) {
 diagStat=YES;
 }else if(buttonIndex==1){
 diagStat=NO;
 }
 }
 */

-(NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {
    
    if (jsonString == nil) {
        return nil;
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers
                                                          error:&err];
    
    if(err) {
        NSLog(@"json解析失败：%@",err);
        return nil;
    }
    
    return dic;
}

-(void)openview:(NSString *)link Title:(NSString *)title
{
    
}
@end