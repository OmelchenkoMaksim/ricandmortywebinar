package com.example.ricandmortyrecycler.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ricandmortyrecycler.R
import com.example.ricandmortyrecycler.models.RickMortyItem

/**
 * В контексте программирования паттерн "Адаптер" представляет собой структурный паттерн проектирования,
 * который позволяет объектам с несовместимыми интерфейсами работать вместе.
 *
 * В контексте RecyclerView (и других компонентов Android, таких как ListView),
 * адаптер используется для преобразования данных из некоторой структуры данных (например, списка, массива или базы данных)
 * в виджеты, которые могут быть отображены в пользовательском интерфейсе.
 *
 * Мост между данными и UI: Адаптер предоставляет способ доступа к данным и их преобразования в виджеты для отображения.
 * В этом смысле адаптер действует как посредник или "адаптер" между структурой данных и RecyclerView.
 */

class CharactersAdapter(
    private val characters: List<RickMortyItem.Character> // данные передаются адаптеру при его создании.
)

/*Это наследование от базового класса RecyclerView.Adapter,
где CharactersAdapter.CharacterViewHolder является типом ViewHolder,
который будет использоваться для отображения каждого элемента списка.

ViewHolder представляет собой конкретный объект,
который хранит ссылки на все подпредставления в одном элементе списка, и который умеет их заполнять.*/
    : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    /**
     * CharacterViewHolder отвечает за хранение представлений элемента списка.
     *
     * В RecyclerView существует понятие пула представлений (view pool), который управляет
     * "переиспользуемыми" представлениями. Когда элемент списка скроллится за пределы экрана,
     * его представление (и соответствующий ему ViewHolder) может быть помещено в этот пул.
     * Позже, когда потребуется новое представление для другого элемента,
     * RecyclerView может взять одно из предварительно созданных представлений из пула вместо создания нового представления с нуля.
     *
     * Однако это не означает, что содержимое или данные представления "кешируются" в ViewHolder.
     * На самом деле ViewHolder просто хранит ссылки на основные компоненты представления
     * (например, TextView, ImageView и т.д.),
     * чтобы избежать повторного поиска этих компонентов в представлении с использованием метода findViewById().
     * Каждый раз, когда RecyclerView переиспользует ViewHolder, адаптер вызывает метод onBindViewHolder(),
     * чтобы "привязать" новые данные к этому ViewHolder (т.е. обновить текст, изображение и т.д. в соответствующем представлении).
     *
     * Таким образом, ViewHolder помогает оптимизировать производительность путем предотвращения
     * повторного поиска компонентов представления, но сам по себе не "кеширует" данные или содержимое этих компонентов.
     */
    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val speciesTextView: TextView = itemView.findViewById(R.id.speciesTextView)
        val imageView: ImageView = itemView.findViewById(R.id.characterImageView)
    }

    /**
     * В этом методе создается новый экземпляр CharacterViewHolder.
     * Вы используете стандартный подход с LayoutInflater, чтобы раздуть макет item_character.
     * Этот макет, вероятно, содержит все необходимые представления, которые упомянуты в CharacterViewHolder.
     *
     * onCreateViewHolder
     *
     * Что создает: Этот метод создает новый ViewHolder,
     * который содержит представление (или макет) для одного элемента в RecyclerView.
     *
     * Куда помещается: Созданный ViewHolder добавляется в пул представлений RecyclerView.
     * Позже, когда пользователь прокручивает список, и RecyclerView нуждается в новом представлении
     * для отображения элемента, он может взять ViewHolder из этого пула.
     *
     * Зачем нужен: Как уже упоминалось, одним из основных преимуществ RecyclerView является
     * эффективное использование ресурсов памяти путем повторного использования представлений.
     * Чтобы достичь этого, RecyclerView создает и хранит определенное количество представлений в пуле,
     * а не создает новое представление для каждого элемента при прокрутке.
     *
     * Сколько раз создается: ViewHolder создается только тогда, когда в пуле представлений нет свободных
     * представлений, которые можно было бы повторно использовать. Как правило, это происходит при первой
     * загрузке списка и при быстрой прокрутке, когда потребность в новых представлениях превышает доступное количество в пуле.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    /**
     * onBindViewHolder
     *
     * Что прикрепляет: Этот метод "привязывает" данные из вашего источника данных
     * (например, списка или массива) к компонентам представления в ViewHolder.
     *
     * К чему прикрепляет: Данные привязываются к конкретным компонентам
     * представления внутри ViewHolder, таким как TextView, ImageView и т.д.
     *
     * Зачем нужен: Как было сказано ранее, одной из ключевых особенностей RecyclerView
     * является повторное использование представлений. Когда представление переиспользуется,
     * его содержимое (текст, изображение и т.д.) также должно быть обновлено, чтобы соответствовать
     * новому элементу. Эта операция выполняется в методе onBindViewHolder.
     *
     * разделение логики создания и привязки позволяет RecyclerView минимизировать количество операций
     * создания представления, повторно используя уже существующие представления
     * и просто обновляя их содержимое при необходимости.
     */
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.nameTextView.text = character.name
        holder.statusTextView.text = character.status
        holder.speciesTextView.text = character.species

//  Glide для загрузки изображений, является хорошей практикой для асинхронной загрузки изображений и кеширования.
        Glide.with(holder.imageView.context)
            .load(character.image)
            .into(holder.imageView)
    }

    /**
     * Этот метод возвращает количество элементов в characters.
     */
    override fun getItemCount() = characters.size
}


/*
ПУЛ ПРЕДСТАВЛЕНИЙ - ВЬЮ ПУЛ

RecyclerView динамически управляет размером пула представлений на основе различных факторов,
включая размер экрана устройства, размер элементов в RecyclerView и скорость прокрутки.
Однако общая идея заключается в том, чтобы хранить достаточное количество представлений в пуле,
чтобы обеспечивать плавную прокрутку, минимизируя при этом издержки на создание новых представлений.

Если говорить более конкретно:

    Минимальный размер пула: Это количество представлений, которые одновременно видимы на экране,
    плюс несколько дополнительных для обеспечения плавной прокрутки. Например, если на экране одновременно видно 5 элементов,
    то минимальное количество представлений в пуле будет немного больше этого числа.

    Максимальный размер пула: Это максимальное количество представлений, которые RecyclerView будет хранить в памяти.
    Это значение может варьироваться, но обычно оно больше, чем минимальное количество, чтобы обеспечивать плавную прокрутку при быстрых движениях.

Вы можете управлять размером пула представлений, используя методы setInitialPrefetchItemCount(int)
и getRecycledViewPool().setMaxRecycledViews(), но для большинства случаев рекомендуется полагаться
на стандартное поведение RecyclerView, который автоматически оптимизирует управление пулом представлений на основе текущих условий.

*/