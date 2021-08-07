/**
 * @file Item.cpp
 * @author Charles B. Owen
 */
#include "pch.h"

#include "Item.h"
#include "Aquarium.h"

/**
 * Constructor
 * @param aquarium The aquarium this item is a member of
 */
Item::Item(Aquarium *aquarium) : mAquarium(aquarium)
{
}

/**
 * Destructor
 */
Item::~Item()
{

}
