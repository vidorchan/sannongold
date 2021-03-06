/**
 * Created by Bright Huang on 10/9/14.
 */
requirejs.config({
    baseUrl : 'js',
    paths: {
        "jquery" : "lib/jquery-2.1.1.min",
        "bootstrap" : "lib/bootstrap-3.0.3.min",
        "handlebars": "lib/handlebars-v2.0.0",
        "pagination": "lib/jquery-pagination",
        "validate" : "lib/jquery-validate-1.13.1",
        "methods": "lib/additional-methods-1.13.1",
        "message_zh": "lib/message_zh.1.13.1",
        "jqueryForm": "lib/jquery-form",
        "sannong" : "app/app",
        "ajaxHandler" : "app/modules/ajax-handler",
        "questionnaire": "app/modules/questionnaire",
        "formValidator": "app/modules/form-validator",
        "region": "app/modules/region",
        "additionalMethods": "app/modules/additional-methods",
        "login": "app/modules/login",
        "clickHandler": "app/modules/click-handler",
        "userManagement": "app/modules/user-management",
        "forgotPassword": "app/modules/forgot-password",
        "userApplicationForm": "app/modules/user-application-form",
        "userPassword": "app/modules/user-password",
        "userProfile": "app/modules/user-profile"

    },
    shim: {
        'bootstrap' : {
            deps : ['jquery'],
            exports : "bootstrap"
        },
        'handlebars' : {
            deps : ['jquery'],
            exports : "handlebars"
        },
        "pagination": {
            deps :['jquery'],
            exports : "pagination"
        },
        "additionalMethods": {
            deps :['jquery'],
            exports : "additionalMethods"
        },
        "validate": {
            deps : ['jquery'],
            exports : "validate"
        },
        "methods": {
            deps : ['jquery'],
            exports : "methods"
        },
        "ajaxHandler": {
            deps : ['jquery'],
            exports : "ajaxHandler"
        },
        "questionnaire": {
            deps : ['handlebars'],
            exports : "questionnaire"
        },
        "applicants": {
            deps : ['jquery'],
            exports : "questionnaire"
        },
        "jqueryForm": {
        	deps :['jquery'],
            exports : "jqueryForm"
        },
        "formValidator": {
            deps :['jquery', 'validate'],
            exports : "formValidator"
        },
        "region": {
            deps :['jquery'],
            exports : "region"
        },
        "login": {
            deps :['jquery'],
            exports : "login"
        },
        "clickHandler": {
            deps :['jquery'],
            exports : "clickHandler"
        },
        "userManagement": {
            deps :['jquery'],
            exports : "userManagement"
        },
        "forgotPassword": {
            deps :['jquery'],
            exports : "forgotPassword"
        },
        "userApplicationForm": {
            deps :['jquery'],
            exports : "userApplicationForm"
        },
        "userPassword": {
            deps :['jquery'],
            exports : "userPassword"
        },
        "userProfile": {
            deps :['jquery'],
            exports : "userProfile"
        }
    }
});








