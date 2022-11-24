package com.esprit.smarktgo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.UpdateTicket
import com.esprit.smarktgo.repository.TicketRepository
import com.esprit.smarktgo.view.WalletDialog
import kotlinx.coroutines.launch

class WalletViewModel(mDialog: WalletDialog): ViewModel() {

    private val mDialog = mDialog

    private val ticketRepository: TicketRepository = TicketRepository()

    fun fill(code: String) : Boolean {
        if(code.isEmpty())
        {
            mDialog.showError("Type the ticket's code!")
            return false
        }
        else if(code.length!=6)
        {
            mDialog.showError("Type a valid code!")
            return false
        }
        else{
            viewModelScope.launch {
                var found = false
                val result = ticketRepository.getAll()
                result.let {
                    for(ticket in result!!)
                    {
                        if(code==ticket.code.toString()&&!ticket.used)
                        {
                            found = true
                            update(code,ticket.value)
                            break
                        }
                        else if(code==ticket.code.toString())
                        {
                            mDialog.showError("Invalid ticket!")
                            found = true
                            break
                        }
                    }
                    if(!found)
                        mDialog.showError("Ticket not found!")
                }
            }
        }
        return true
    }

    private fun update(code: String,ticketValue:Int)
    {
        viewModelScope.launch {
            val updateTicket = UpdateTicket(code.toInt(),mDialog.userId,mDialog.wallet)
            val result = ticketRepository.update(updateTicket)
            result?.let {
                mDialog.dismiss(mDialog.wallet+ticketValue)
            }
        }
    }

}