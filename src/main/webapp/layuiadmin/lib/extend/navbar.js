/**
 * 自动生成菜单
 * 当前最多支持到第三级菜单
 * 起始方法 navber.render(options)
 * options = {
 * 		menuTitle:"",//菜单标题
 * 		elem: "",//菜单layui-side-scroll的ID
 * 		cached: false,//是否使用缓存
 *  	//data: menu,//如果已获取到数据，可直接使用，和url替换。格式：var menu = {"":"",...}
 *  	url: layui.setter.base+"json/menu.json"	//获取菜单数据的地址。和data共存时，默认使用data
 * }
 * 
 * author：HL time:2018/06/28
 */
layui.define(['element','jquery'], function (exports) {
    "use strict";
    var $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        element = layui.element,
        cacheName = 'tb_navbar';
    
    var navbar = {}
    
    /**
     * 菜单加载入口
     */
    navbar.render = function (options) {
    	var menuTitleHTML = '<div class="layui-logo"><span>'+options.menuTitle+'</span></div>'
    	
        if (typeof (options.elem) !== 'string' && typeof (options.elem) !== 'object') {
        	console.log('Navbar error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var $container;
        if (typeof (options.elem) === 'string') {
            $container = $('' + options.elem + '');
        }
        if ($container.length === 0) {
        	console.log('Navbar error:找不到elem参数配置的容器，请检查.');
        }
        if (options.data === undefined && options.url === undefined) {
        	console.log('Navbar error:请为Navbar配置数据源.')
        }
        if (options.data !== undefined && typeof (options.data) === 'object') {
        	navbar.setFirstPage(options.data);
            var html = menuTitleHTML + navbar.getHtml(options.data);
            $container.html(html);
            element.init();
            config.elem = $container;
        } else {
            if (options.cached) {
                var cacheNavbar = layui.data(cacheName);
                if (cacheNavbar.navbar === undefined) {
                    $.ajax({
                    	type: "GET",
            			async : false,
            			url: options.url,
            			data : {},// 参数
            			dataType : "json",
            			success : function(result, status) {
            				if(result.success){
            					// 添加缓存
                                layui.data(cacheName, {
                                    key: 'navbar',
                                    value: result
                                });
                                navbar.setFirstPage(result.data);
                                var html = menuTitleHTML + navbar.getHtml(result.data);
                                $container.html(html);
                                element.init();
            				}
            				
            			},
            			error : function(XMLHttpRequest, textStatus, errorThrown) {
            				console.log('Navbar error:' + errorThrown);
            			},
            			complete: function (xhr, status) {
                        	options.elem = $container;
                        }
            		});
                } else {
                	navbar.setFirstPage(cacheNavbar.navbar);
                    var html = menuTitleHTML + navbar.getHtml(cacheNavbar.navbar);
                    $container.html(html);
                    element.init();
                    options.elem = $container;
                }
            } else {
                // 清空缓存
                layui.data(cacheName, null);
                $.ajax({
                    type:"GET",
                    url: options.url,
                    async: false, // _config.async,
                    dataType: 'json',
                    success: function (result, status) {
                    	if(result.success){
                    		navbar.setFirstPage(result.data);
                    		var html = menuTitleHTML + navbar.getHtml(result.data);
                            $container.html(html);
                            element.init();
                    	}
                    },
                    error: function (xhr, status, error) {
                    	console.log('Navbar error:' + error);
                    },
                    complete: function (xhr, status) {
                        options.elem = $container;
                    }
                });
            }
        }
    };

	
	/**
	 * 获取html字符串
	 * 
	 * @param {Object}
	 *            data
	 */
    navbar.getHtml = function(data) {
   
        var ulHtml = '<ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu" lay-filter="layadmin-system-side-menu">{li}</ul>';
        
        var liHtml = '<li class="layui-nav-item {itemClass}">{aOrdl}</li>';
        var liItemedClass = 'layui-nav-itemed';
        var liItemClass = 'layui-nav-item';
        
        var aHtml = '<a href="javascript:;" lay-tips="{title}" lay-direction="2" {layHref}>{ai}</a>';
        var aLayHrefHtml = 'lay-href="{url}"';
        var iHtml = '<i class="layui-icon {icon}"></i> <cite>{title}</cite>';
        
        var dlHtml = '<dl class="layui-nav-child">{dd}</dl>';
        
        var ddHtml = '<dd data-name="{name}" class="{layuiThisClass}"><a href="javascript:;" {ddLayHrefHtml}>{title}</a>{dddl}</dd>';
        var ddLayHrefHtml = 'lay-href="{url}"';
        var layuiThisClass = 'layui-this';
        // 遍历数据，即循环生成<li>菜单
        var htmlLi = "";
		var htmlLiCount = "";
        var htmlAOrDl = "";
		var firstMenu;//一级菜单
        for (var i = 0; i < data.length; i++) {
			firstMenu = data[i];
        	// 第一个展开模式
            if (i == 0) {
            	htmlLi = liHtml.replace(/\{itemClass\}/g,liItemedClass);
            } else {
            	htmlLi = liHtml.replace(/\{itemClass\}/g,liItemClass);
            }
            
            // 组建li下a标签
            var htmlI = iHtml.replace(/\{icon\}/g,firstMenu.icon).replace(/\{title\}/g,firstMenu.title);
            var htmlA = "";
			var htmlDl = "";
            var htmlDd = "";
            // 判断一级菜单是否存在子级
            if (firstMenu.children !== undefined && firstMenu.children !== null && firstMenu.children.length > 0) {
                htmlA = aHtml.replace(/\{title\}/g,firstMenu.title).replace(/\{layHref\}/g,'');
                
                var htmlDdLayHref = null;
                var childrenData = {};
                for(var j = 0; j < firstMenu.children.length; j++ ){
                	childrenData = firstMenu.children[j];
                	
                	// 判断是否拥有第三级
					var htmlDdDl = "";
					var htmlDdDldd = "";
					var htmlDlDdLayHref
                	if (childrenData.children !== undefined && childrenData.children !== null && childrenData.children.length > 0) {
                		htmlDdLayHref = '';
                		var endData = {};
                		for(var k = 0; k < childrenData.children.length; k++){
                			endData = childrenData.children[k];
							htmlDlDdLayHref = ddLayHrefHtml.replace(/\{url\}/g,endData.url);
                			if(i == 0 && j == 0 && k == 0){
                				htmlDdDldd += ddHtml.replace(/\{name\}/g,endData.name).replace(/\{layuiThisClass\}/g,layuiThisClass)
                        					.replace(/\{title\}/g,endData.title).replace(/\{ddLayHrefHtml\}/g,htmlDlDdLayHref)
                        					.replace(/\{dddl\}/g,'');
                        	}else{
                        		htmlDdDldd += ddHtml.replace(/\{name\}/g,endData.name).replace(/\{layuiThisClass\}/g,'')
                        					.replace(/\{title\}/g,endData.title).replace(/\{ddLayHrefHtml\}/g,htmlDlDdLayHref)
                        					.replace(/\{dddl\}/g,'');
                        	}
                		}
                		// 第三级的<dl>
                		htmlDdDl = dlHtml.replace(/\{dd\}/g,htmlDdDldd);
                	}else{
                		htmlDdDl = '';
                		htmlDdLayHref = ddLayHrefHtml.replace(/\{url\}/g,childrenData.url);
                	}
                	
                	if(i == 0 && j == 0){
                		//htmlDdDl == '' ? layuiThisClass:liItemedClass 
                		//判断二级菜单是否含有子级，样式是“选中” 还是 “展开”
                		htmlDd += ddHtml.replace(/\{name\}/g,childrenData.name).replace(/\{layuiThisClass\}/g,htmlDdDl == '' ? layuiThisClass:liItemedClass)
                					.replace(/\{title\}/g,childrenData.title).replace(/\{ddLayHrefHtml\}/g,htmlDdLayHref)
                					.replace(/\{dddl\}/g,htmlDdDl);
                	}else{
                		htmlDd += ddHtml.replace(/\{name\}/g,childrenData.name).replace(/\{layuiThisClass\}/g,'')
                					.replace(/\{title\}/g,childrenData.title).replace(/\{ddLayHrefHtml\}/g,htmlDdLayHref)
                					.replace(/\{dddl\}/g,htmlDdDl);
                	}
                	
                }
                // 将第二级的<dd>标签，装入一级下的<dl>
                htmlDl = dlHtml.replace(/\{dd\}/g,htmlDd);
				
               
            }else{
            	var htmlALayHref = aLayHrefHtml.replace(/\{url\}/g,firstMenu.url)
            	htmlA = aHtml.replace(/\{title\}/g,firstMenu.title).replace(/\{layHref\}/g,htmlALayHref);
            }
            htmlA = htmlA.replace(/\{ai\}/g,htmlI);
            
            // 获取所有<li>
            htmlAOrDl = htmlA + htmlDl;
			
            htmlLiCount += htmlLi.replace(/\{aOrdl\}/g,htmlAOrDl);
        }
        ulHtml = ulHtml.replace(/\{li\}/g,htmlLiCount);
        
        return ulHtml;
    }
    
    navbar.setFirstPage = function(data){
    	var firstPage = navbar.getFirstPage(data);
    	//右侧第一个默认标签
    	$("#LAY_app_tabsheader_li").attr("lay-id",firstPage.url)
    	$("#LAY_app_tabsheader_li").attr("lay-attr",firstPage.url)
    	$("#LAY_app_tabsheader_li").text(firstPage.title)
    	//右侧主页面
    	$("#LAY_app_body_iframe").attr("src",firstPage.url)
    }
    
    navbar.getFirstPage = function(data){
    	var firstPage = "";
    	if(data[0].children !== undefined && data[0].children !== null && data[0].children.length > 0){
    		data = data[0].children;
    		firstPage = navbar.getFirstPage(data);
    	}else{
    		firstPage = data[0];
    	}
    	return firstPage;
    }

    exports('navbar', navbar);
});