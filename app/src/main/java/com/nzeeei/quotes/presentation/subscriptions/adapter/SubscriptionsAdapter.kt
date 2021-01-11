package com.nzeeei.quotes.presentation.subscriptions.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.nzeeei.quotes.databinding.ItemSubscriptionBinding
import com.nzeeei.quotes.presentation.subscriptions.SubscriptionsViewItem
import javax.inject.Inject

fun subscriptionAdapterDelegate(checkChangedListener: (String, Boolean) -> Unit) =
    adapterDelegateViewBinding<SubscriptionsViewItem.Subscription, SubscriptionsViewItem, ItemSubscriptionBinding>(
        { layoutInflater, root -> ItemSubscriptionBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                quote.text = item.data.displayName
                root.isChecked = item.data.subscribed
                root.setOnCheckedChangeListener { _, checked ->
                    checkChangedListener.invoke(item.data.id, checked)
                }
                root.setOnClickListener {
                    root.isChecked = !root.isChecked
                }
            }
        }
    }

class SubscriptionsAdapter @Inject constructor(
    checkChangedListener: (String, Boolean) -> Unit
) : ListDelegationAdapter<List<SubscriptionsViewItem>>(
    subscriptionAdapterDelegate(checkChangedListener)
)
