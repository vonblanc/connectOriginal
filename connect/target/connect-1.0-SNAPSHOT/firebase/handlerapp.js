$("document").ready(function(){

var model={

    currentUser: null,

    currentUserId: null,


    serviceForm: null,

    profile:null,

    firebaseToken: null,

    init: function(){

     model.profile={

	    "profileName":"Lourens Hairdresser",
		   
		"phoneNumber":"07761006970" ,
		
		"serviceType": [
			   "HAIRDRESSER",
	           "BEAUTICIAN", 
	           "MASSUER",
	           "MANICURE SPECIALIST",
	           "BEAUTY BLOGGER",
			   ] ,
		
		
		"literalAddress":"14 Booth close thamesmead",
		
		"postCode":"SE28 8BW",
		
		"startTime":"07:00",
		
     	 "finishTime":"20:00",
	   
    };


    },

};


var octupus={

  checkSignedInState: function(){
  
   firebase.auth().onAuthStateChanged(function(user){
              
     if(user) 
      { 
           if(user.uid!=model.currentUserId){    //deal with case where current user is different from signed in user
                model.currentUser=user;
                model.currentUserId=user.uid;

                view.render(model.currentUser);
             }

            model.currentUser=user;

            console.log("User is "+ JSON.stringify(model.currentUserId));
            octupus.getAuthToken();  //the get token async function in the octupus calls after user is signed in

            
      };



  })},

  signOut: function(){


       firebase.auth().signOut().then(function(){
           
        model.currentUser=null;
        model.currentUserId=null;
        view.render(model.currentUser);

        });
  },
  

  getAuthToken: function(){
  
   
    firebase.auth().currentUser.getToken().then(function(token){
		
        console.log("Successfully got Token from firebase:"+token );
         model.firebaseToken=token;
        

	},function(error){console.log(error);});



  },

  createProfile:function(){
    var type=requests.post;
    var path=requests.createProfilePath;   /////////////////////////////////////
    var url=requests.appUrl;
    var data=model.profile;
    console.log("Token record in model: "+model.firebaseToken);
    var token=model.firebaseToken;   
    requests.sendRequest(type,path,data,token,"");

  },

init: function(){
 
 model.init();
 octupus.checkSignedInState();  

 view.init();

 requests.init();



},

};


var view={

init: function(){

    this.name=$('#name');
    this.email=$('#email');      //initializzes the user display section
    this.header=$('h3');
    this.formButton=$("#send");
    this.myForm=$("#serviceForm");
    this.createProfileButton=$("#send_token").find("button");
    this.loadingBar=$("#loading");
    this.signOutButton=$('#sign_out').find("button");
    this.render();



    this.myForm=$("#serviceForm");   
    this.myFormName=$("#serviceName");
    this.myFormPrice=$("#servicePrice");
    this.myFormCategory=$("#serviceCategory");       //renders the service form
    this.myFormDescription=$("#serviceDescription");
    this.renderForm();
},

render: function(user)
       {
               if(user)
            {    
               $(".hidden").css("display","block");  //select all hidden classes
               
                view.name.text(user.displayName);
                console.log("display name "+view.name.text());   
                view.email.text(user.email);
                console.log("display email "+view.email.text());

                view.loadingBar.toggleClass("hidden"); 
            };

            if(user===null)
                {
                      $("h3").text("Signed Out");
	  	              $(".hidden").css("display","none");
	                  console.log("Sign-Out Sucessful");
                   
                };

               view.createProfileButton.on('click',function(){
                  octupus.createProfile();
               });
           
                view.signOutButton.on('click',function(){
                  octupus.signOut();
                 });

        },




    renderForm: function(){
       

     this.myForm.toggleClass('hidden_form');

     this.myForm.submit(function(event){
        
       
       event.default();

     });
     

    },


};

var requests={
 
    init: function(){
    this.appUrl='http://localhost:8080/_ah/api/connectapi/v1/';
    this.post='POST';
    this.get='GET';
    this.createProfilePath="createProfile";
	this.updateProfilePath="updateProfile";
	this.createServicePath="createService";
	
    },

    sendRequest: function(requestType,requestPath,requestData,headerToken,urlAppendages){


     	$.ajax({

	          url: 'http://localhost:8080/_ah/api/connectapi/v1/'+requestPath+urlAppendages,                                //'?webSafeProfileKey='+ webSafeProfileKey,

	          type: requestType,
	          
	          cache:false,
	          
	          contentType: 'application/json', 

	         

	          data: JSON.stringify(requestData),

	          success: function (data, textStatus, xhr) {
                   return data;
	          },
	          
	          headers:{
	       	   'Authorization': 'Bearer '+headerToken
	       	   
	       	   
	          },

	          error: function (xhr, textStatus, errorThrown) {
	              
	       	   
	              console.log('Error in Operation');
	              console.log(textStatus + "Error thrown:"+ errorThrown);
	              
	          }

	      });
	  	   
	  	   
      },
          
      


    }






 octupus.init();



});