package com.example.laknews.Models

class AppUser (val uid:String, val email:String, val profileImageUrl:String, val interest:String){
    constructor() : this("","","","")
}