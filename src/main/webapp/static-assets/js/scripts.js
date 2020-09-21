
jQuery(document).ready( function() {
	
	// Check for click events on the navbar burger icon
	$(".navbar-burger").click(function() {
	
		// Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
		$(".navbar-burger").toggleClass("is-active");
		$(".navbar-menu").toggleClass("is-active");
	});
	  
	// make iframes full height
	$('.oapen-snippet iframe').on("load", function() {

		var iframe = $(window.top.document).find(".oapen-snippet iframe");
		iframe.height(iframe[0].ownerDocument.body.scrollHeight+'px' );
	});	  
	  
	$(".ajaxloader").each( function() {
		
		var src = $(this).attr("data-src");
		$(this).html(" <i class='fa fa-spinner fa-pulse fa-fw'></i><i>&nbsp;connecting to library...</i>" );
		$(this).load( src, function() {
			// whatever
		});				
	});	  

	$(".oapen-toolkit-tree ul>li>a").each( function() {
		
		$(this).click(function(e) {
			//$(".clickloader").addClass("is-hidden");
			var target = $(e.target).siblings(".clickloader")
			$(".clickloader").not(target).addClass("is-hidden");
			$(".clickloader").not(target).parent().removeClass("is-foldout");
			var src = $(target).attr("data-src");
			if ($(target).children().length==0) {
				$(target).html(" <i class='fa fa-spinner fa-pulse fa-fw'></i><i>&nbsp;loading&hellip;</i>" );
				$(target).load( src, function() {});
			}
			$(target).toggleClass("is-hidden");
			$(this).parent().toggleClass("is-foldout")
			return false;
		});
	});
	
	// wrap content tables in a horizontal scrolling div
	$(".content table").wrap("<div class='oapen-table-wrapper'></div>");
	$("<div class='oapen-table-swipe'>swipe to view table</div>").insertBefore($(".oapen-table-wrapper"));
	
	
	/* Shrink font size for extremely long titles */
	$('.content h1').each(function() {
		var el= $(this);
		var textLength = el.html().length;
		if (textLength > 50) {
			el.css('font-size', '2.1rem');
		}
	});
	
	
});

/*
function lifecycle(n) {
	alert("you clicked " + n + "!");
}*/


