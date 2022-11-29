package com.esprit.smarktgo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.UpdateTicket
import com.esprit.smarktgo.repository.TicketRepository
import com.esprit.smarktgo.view.WalletActivity
import kotlinx.coroutines.launch

class WalletViewModel(mActivity: WalletActivity): ViewModel() {

    private val mActivity = mActivity

    private val ticketRepository: TicketRepository = TicketRepository()

    fun fill(code: String) : Boolean {
        if(code.isEmpty())
        {
            mActivity.showError("Type the ticket's code!")
            return false
        }
        else if(code.length!=6)
        {
            mActivity.showError("Type a valid code!")
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
                            update(code)
                            break
                        }
                        else if(code==ticket.code.toString())
                        {
                            mActivity.showError("Invalid ticket!")
                            found = true
                            break
                        }
                    }
                    if(!found)
                        mActivity.showError("Ticket not found!")
                }
            }
        }
        return true
    }

    private fun update(code: String)
    {
        viewModelScope.launch {
            val updateTicket = UpdateTicket(code.toInt(),mActivity.id,mActivity.wallet)
            val result = ticketRepository.update(updateTicket)
            result?.let {
                mActivity.finish()
            }
        }
    }

}