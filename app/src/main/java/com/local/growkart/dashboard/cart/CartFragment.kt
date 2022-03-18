package com.local.growkart.dashboard.cart

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.local.growkart.GrowKartApp
import com.local.growkart.R
import com.local.growkart.dashboard.adapter.CartAdapter
import com.local.growkart.dashboard.model.Product
import com.local.growkart.databinding.FragmentCartBinding
import com.local.growkart.order.OrderMapper
import com.local.growkart.util.SpannableUtil
import com.local.growkart.util.StringUtil
import com.local.growkart.util.ToastUtil
import com.local.growkart.util.ui.FirstLastSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding
    lateinit var adapter: CartAdapter
    lateinit var navController: NavController
    private val viewmodel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        binding = DataBindingUtil.bind(root)!!
        viewmodel.init()
        initCartList()
        initErrorContainer()
        return root
    }

    private fun hideProgressBar() {
        binding.showProgress = false
    }

    private fun showProgress() {
        binding.showProgress = true
    }

    private fun setNavigation() {
        if (!this::navController.isInitialized)
            navController = findNavController()
        //Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    private fun initErrorContainer() {
        val emptyCartMessage = getString(R.string.no_product_in_cart_message)
        val store = getString(R.string.store)
        val spannedMessage = SpannableString(emptyCartMessage)
        val message = spannedMessage.apply {
            SpannableUtil.addBoldPart(this, emptyCartMessage, store)
            SpannableUtil.addClickablePart(
                this,
                emptyCartMessage,
                store,
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            ) {
                navController.navigate(R.id.action_cart_to_home)
            }
        }
        binding.errorMessage.movementMethod = LinkMovementMethod.getInstance()
        binding.errorMessage.text = message
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigation()
        initUserSessionListener()
        initCartListener()
        initPlaceOrder()
    }

    private fun initUserSessionListener() {
        viewmodel.getUserSessionStatus().observe(viewLifecycleOwner, {
            if (!it) {
                hideProgressBar()
                ToastUtil.showErrorWithAction(
                    requireActivity(),
                    "You need to login to place the order",
                    "Login"
                ) { navController.navigate(R.id.action_cart_to_loginFragment) }
            }
        })
    }

    private fun initPlaceOrder() {
        binding.btnPlaceOrder.setOnClickListener {
            showProgress()
            viewmodel.createOrder(
                {
                    view?.postDelayed({
                        hideProgressBar()
                        val action =
                            CartFragmentDirections.actionCartToOrderDetail(viewmodel.convertCartToOrder())
                        navController.navigate(action)
                        viewmodel.emptyCart()
                    }, 2000)

                }) {
                hideProgressBar()
                ToastUtil.showErrorInfo(
                    requireActivity(),
                    "Something went wrong while placing order! Try again!"
                )
            }

        }
    }

    private fun initCartListener() {
        viewmodel.getCart().observe(viewLifecycleOwner, {
            if (it.size == 0)
                binding.isCartEmpty = true
            else {
                binding.isCartEmpty = false
                adapter.updateProducts(it)
            }
        })
        viewmodel.getTotalcost().observe(viewLifecycleOwner, {
            binding.cartTotal = "${StringUtil.getCurrencySymbol()} $it"
        })
    }


    val cartCallBack: CartAdapter.CartChangeListener = object : CartAdapter.CartChangeListener {
        override fun onQtyChange(product: Product, qty: Int) {
            viewmodel.changeProductQuantity(CartModel(product, qty))
        }

        override fun onDelete(product: Product) {
            viewmodel.deleteProductFromCart(product)
        }

    }

    private fun initCartList() {
        binding.productList.layoutManager = LinearLayoutManager(requireContext())
        binding.productList.addItemDecoration(
            FirstLastSpacingItemDecoration(
                resources.getDimensionPixelOffset(
                    R.dimen.dimen_16
                ), 0, FirstLastSpacingItemDecoration.Mode.VERTICAL
            )
        )
        adapter = CartAdapter()
        adapter.listener = cartCallBack
        binding.productList.adapter = adapter
    }

}