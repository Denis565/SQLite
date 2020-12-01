package com.example.sqlite

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var peopleDB:DatabaseHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        peopleDB= DatabaseHelper(this@MainActivity)

        btnAdd.setOnClickListener {
            addUser()
        }
        btnShow.setOnClickListener {
            showUsers()
        }
        btnRemove.setOnClickListener {
            removeUser()
        }
        btnUpdate.setOnClickListener {
            updateUserInformation()
        }
        
        btnBring.setOnClickListener {
            bringUserEdit()
        }
    }


    private fun addUser() {
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()

        if (edtID.text.toString()!="") {
            display("Нельзя так делать","При добавлении данных не нужно задавать ID")
        }
        else {
            if (name == "" || email == "") {
                display(
                    "Нельзя так делать",
                    "При добавлении данных нужно задавать значения для полей Имя и E-mail "
                )
            } else {

                val insertData: Boolean = peopleDB!!.addData(name, email)
                if (insertData) {
                    Toast.makeText(this@MainActivity, "Запись добавлена!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Что-то пошло не так", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun showUsers() {
        val data:Cursor?=peopleDB!!.showData()

        if (data!!.count==0){
            display("Error","Нет данных")
            return
        }

        val buffer=StringBuffer()

        while (data.moveToNext()){
           buffer.append("ID:"+data.getString(0)+"\n")
            buffer.append("Name:"+data.getString(1)+"\n")
            buffer.append("Email:"+data.getString(2)+"\n"+"\n")
        }
        display("Все пользователи",buffer.toString())
    }

    fun updateUserInformation(){
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()
        val id=edtID.text.toString()

        if (name!="" && email!=""&& id!="") {
            val update: Boolean = peopleDB!!.update(name, email, id.toInt())
            if (update) {
                Toast.makeText(this@MainActivity, "Запись обновлена!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Что-то пошло не так", Toast.LENGTH_LONG).show()
            }
        }else{
            display("Нельзя так делать","При обнавлении данных все поля должны быть заполнены.")
        }
    }

    fun removeUser(){

        val id=edtID.text.toString()

        if (id!="") {
            val remove: Boolean = peopleDB!!.remove(id.toInt())
            if (remove) {
                Toast.makeText(this@MainActivity, "Запись удалена!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Что-то пошло не так", Toast.LENGTH_LONG).show()
            }
        }else{
            display("Нельзя так делать","Для удаления поле id должно быть заполнено.")
        }
    }

    private fun bringUserEdit() {
        var trueData=0
        if (edtID.text.toString()=="" || edtID.text.toString().toLongOrNull()==null)
        {
            display("Нельзя так делать","Для вывода полей нужен ID и он доллжен иметь вид числа")
        }
        else
        {
            val data:Cursor?=peopleDB!!.showData()

            if (data!!.count==0){
                display("Error","Нет данных")
                return
            }

            while (data.moveToNext()){
                if (data.getString(0)==edtID.text.toString())
                {
                    edtName.setText(data.getString(1))
                    edtEmail.setText(data.getString(2))
                    Toast.makeText(this@MainActivity, "Данные выведенны", Toast.LENGTH_LONG).show()
                    trueData=1;
                    break
                }

            }
            if (trueData==0){
                Toast.makeText(this@MainActivity, "Такой записи не существует введите другой ID", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun display(title:String?,message: String?){
        val builder:AlertDialog.Builder=AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("ОК"){ _, _ -> }
        builder.show()
    }
}