package com.example.shoppinglist.presentation


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID


    /**
    TODO #23.1

    onEditingFinishedListener - нельзя инициализировать из вне(напр из MainActivity и пр), так как при пересоздании
    фрагмента(смена ориентации и пр) значение переменных сбрасываются.
    Activity, содержащая фрагмент обязана реализовывать его интерфейс. Если она это не сделает - бросаем исключение.
    context is OnEditingFinishedListener - так как activity реализует этот интерфейс.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener.")
        }
        Log.d("ShopItemFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
        Log.d("ShopItemFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ShopItemFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
        Log.d("ShopItemFragment", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("ShopItemFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ShopItemFragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ShopItemFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ShopItemFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ShopItemFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShopItemFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("ShopItemFragment", "onDetach")
    }

    /**
    TODO #18.7

    Подписываемся на обновления объектов viewModel для показа ошибок в текстовых полях ввода, если введённые значения
    отсутствуют или не валидны и показываем ошибку.
    Подписываемся на обновления объекта viewModel для закрытия экрана.
     */
    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    /**
    TODO #18.4

    Установка режима экрана в зависимости от значения переменной screenMode.
     */
    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    /**
    TODO #18.3

    Если в текстовое поле начинают вводить текст, убираем сообщение об ошибке. Устанавливаем слушатели ввода текста
    у полей ввода. Подписываемся на методы resetErrorInputName() и resetErrorInputCount() из ShopItemViewModel.
     */
    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    /**
    TODO #18.6

    Режим редактирования элемента.
     */
    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) { shopItem ->
            etName.setText(shopItem.name)
            etCount.setText(shopItem.count.toString())

        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())

        }
    }

    /**
    TODO #18.5

    Режим добавления элемента.
     */
    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    /**
    TODO #18.2

    Парсим полученные аргументы. Инициализируем переменные screenMode и shopItemId.
     */
    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.ed_name)
        etCount = view.findViewById(R.id.ed_count)
        buttonSave = view.findViewById(R.id.save_button)
    }

    /**
    TODO #23

    Если fragment должен о чём-то сообщить activity, то делаем это через интерфейс.
    Этот интерфейс реализует Activity, к которой прикреплён данный фрагмент.
     */
    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        /**
        TODO #19

        Константы делаем приватными.
        Создаём два статических метода, каждый из которых возвращает ShopItemFragment.
        Во фрагменты интенты отправлять не стоит, передавать значения нужно с помощью Bundle.
        В аргументы(arguments) кладём Bundle, в который складываем нужные значения.
         */
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}