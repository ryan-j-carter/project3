/* 
    CS 137
    Project 3
    Group 1

    scripts.js
 */


 /*
	window_onload() and navbar_reset_top()
	Check the navbar position on scroll,
	if it would leave the screen, lock it to 
	the top of the window.
 */
 function window_onload() {
 	window.addEventListener("scroll", navbar_reset_top, false);
 }
 
 function call_item_post(id) {
     var request = new XMLHttpRequest();
     request.open("POST", "/project3/item?id="+id, true);
     request.send(null);
 }

 var m=50; //Must be equal to margin-top for overview.

 function navbar_reset_top() {
 	var headerOffset=header.getBoundingClientRect();
 	var bottom=headerOffset.bottom;
 	var navOffset=nav.getBoundingClientRect();
 	var h=navOffset.height;

 	if(bottom<=0&&nav.className==="nav_rel") {
 		document.getElementById("nav").className="nav_fixed";
 		document.getElementById("firstcontent").style.marginTop=m+h+"px";
 	}
 	else if (bottom>0&&nav.className==="nav_fixed") {
 		document.getElementById("nav").className="nav_rel";
 		document.getElementById("firstcontent").style.marginTop=m+"px";
 		
 	}
 }

var query_param = function(field) {
	var href = window.location.href;
	var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i');
	var val = reg.exec(href);
	return val ? val[1] : "0";
};

/*
	set_shipping()
	When checkbox is checked, set shipping address equal to billing address
	When checkbox is unchecked, clear shipping address
*/
function set_shipping() {
	var frm = document.checkout_form;
	if (frm.sameship.checked) {
		frm.addr_b.value = frm.addr_s.value;
		frm.city_b.value = frm.city_s.value;
		frm.state_b.value = frm.state_s.value;
		frm.zip_b.value = frm.zip_s.value;
		frm.phone_b.value = frm.phone_s.value;
	}
	else {
		frm.addr_b.value = "";
		frm.city_b.value = "";
		frm.state_b.value = "";
		frm.zip_b.value = "";
		frm.phone_b.value = "";
	}
}

function form_reset() {
	var frm = document.checkout_form;
	frm.quantity.value = "1";

	frm.fname.value = "";
	frm.lname.value = frm.lname.defaultValue;

	frm.addr_b.value = frm.addr_b.defaultValue;
	frm.state_b.value = frm.state_b.defaultValue;
	frm.city_b.value = frm.city_b.defaultValue;
	frm.zip_b.value = frm.zip_b.defaultValue;
	frm.phone_b.value = frm.phone_b.defaultValue;

	frm.sameship.checked = false;

	frm.addr_s.value = frm.addr_s.defaultValue;
	frm.state_s.value = frm.state_s.defaultValue;
	frm.city_s.value = frm.city_s.defaultValue;
	frm.zip_s.value = frm.zip_s.defaultValue;
	frm.phone_s.value = frm.phone_s.defaultValue;

	document.getElementById("def_payment").checked = true;

	frm.cardnumber.value = frm.cardnumber.defaultValue;
	frm.nameoncard.value = frm.nameoncard.defaultValue;
	frm.month.value = "0";
	frm.year.value = "0";
	frm.securitycode.value = frm.securitycode.defaultValue;

	document.getElementById("def_shipping").checked = true;
}

function has_num(n) {
	return n.match(/\d+/g) != null;
}

function submit_order() {
    var frm = document.checkout_form;
    //Forced to create variables and debug because SQL statements are finicky
    var request = new XMLHttpRequest();
    var first_name = frm.fname.value;
    var last_name = frm.lname.value;
    var ship_address = frm.addr_s.value;
    var ship_city = frm.city_s.value;
    var ship_state = frm.state_s.value;
    var ship_zip = frm.zip_s.value;
    var ship_phone = frm.phone_s.value;
    var bill_address = frm.addr_b.value;
    var bill_city = frm.city_b.value;
    var bill_state = frm.state_b.value;
    var bill_zip = frm.zip_b.value;
    var bill_phone = frm.phone_b.value;
    var str = document.querySelector('input[name="payment"]:checked').value;
    var payment_method;
    if (str == "visa") {
        payment_method = "Visa";
    }
    else if (str == "master") {
        payment_method = "MasterCard";
    }
    else if (str == "discover") {
        payment_method = "Discover";
    }
    else {
        payment_method = "Master Card";
    }
    var card_number = frm.cardnumber.value;
    var card_name = frm.nameoncard.value;
    var card_expiration_month = frm.month.options[frm.month.selectedIndex].value;
    var card_expiration_year = frm.year.options[frm.year.selectedIndex].value;  
    var card_security = frm.securitycode.value;
    var ship_method = frm.shipping_method.options[frm.shipping_method.selectedIndex].value;
    
    request.open("POST", "/project3/checkout?"
                +"first_name="+first_name
                +"&last_name="+last_name
                +"&ship_address="+ship_address
                +"&ship_city="+ship_city
                +"&ship_state="+ship_state
                +"&ship_zip="+ship_zip
                +"&ship_phone="+ship_phone
                +"&bill_address="+bill_address
                +"&bill_city="+bill_city
                +"&bill_state="+bill_state
                +"&bill_zip="+bill_zip
                +"&bill_phone="+bill_phone
                +"&payment_method="+payment_method
                +"&card_number="+card_number
                +"&card_name="+card_name
                +"&card_expiration_month="+card_expiration_month
                +"&card_expiration_year="+card_expiration_year
                +"&card_security="+card_security
                +"&ship_method="+ship_method, true);
    
    request.send(null);
}

/*
	form_validate()
	Makes sure every field contains some value.
	Ensures certain fields are only numeric or only alphabetic.
*/

function form_validate(order_id) {
	var messages = [];
	var frm = document.checkout_form;
	if (frm.fname.value === "") {messages.push("Enter your first name.");}
	if (frm.lname.value === "") {messages.push("Enter your last name.");}
	if (frm.addr_b.value === "") {messages.push("Enter an address in Billing.");}
	if (frm.city_b.value === "") {messages.push("Enter a city in Billing.");}

	if (frm.state_b.value.length < 2 || has_num(frm.state_b.value)) {
		messages.push("Enter a valid state in Billing.");
	}
	if (frm.zip_b.value.length < 5 || isNaN(frm.zip_b.value)) {
		messages.push("Enter a valid zip code in Billing.");
	}
	if (frm.phone_b.value.length < 10 || isNaN(frm.phone_b.value)) {
		messages.push("Enter a valid phone number in Billing.");
	}

	if (frm.addr_s.value === "") {messages.push("Enter an address in Shipping.");}
	if (frm.city_s.value === "") {messages.push("Enter a city in Shipping.");}

	if (frm.state_s.value.length < 2 || has_num(frm.state_s.value)) {
		messages.push("Enter a valid state in Shipping.");
	}
	if (frm.zip_s.value.length < 5 || isNaN(frm.zip_s.value)) {
		messages.push("Enter a valid zip code in Shipping.");
	}
	if (frm.phone_s.value.length < 10 || isNaN(frm.phone_s.value)) {
		messages.push("Enter a valid phone number in Shipping.");
	}

	if (frm.cardnumber.value.length < 16 || isNaN(frm.cardnumber.value)) {
		messages.push("Enter a valid card number.");
	}
	if (frm.nameoncard.value === "") {messages.push("Enter a card number.");}

	if (frm.month.value === "0") {
		messages.push("Select a month.");
	}
	if (frm.year.value === "0") {
		messages.push("Select a year.");
	}
	if (frm.securitycode.value.length < 3 || isNaN(frm.securitycode.value)) {
		messages.push("Enter a valid security code.");
	}
	if (messages.length > 0) {
		if (messages.length > 5) {alert("Please fill out the form.");}
		else {alert(messages.join('\n'));}
		return false;
	}
	submit_order();
        window.location.href="/project3/order.jsp?order_id="+order_id;
	
	return true;
}

