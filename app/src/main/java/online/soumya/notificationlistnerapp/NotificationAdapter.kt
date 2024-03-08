package online.soumya.notificationlistnerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import online.soumya.notificationlistnerapp.databinding.IncomingNotificationBinding

class NotificationAdapter(private val context: Context, val notifications: MutableList<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    inner class ViewHolder(var binding:IncomingNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = IncomingNotificationBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.binding.txtSenderName.text = notification.text
        holder.binding.txtmessage.text = notification.title
        holder.binding.txtApplicatioName.text = notification.packageName
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun add(notification: NotificationModel) {
        notifications.add(notification)
        notifyDataSetChanged()
    }
}